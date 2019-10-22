import groovy.json.JsonSlurper

// The following parameters are injected by Maven
//def outputFolder = "output"
//def templateFolder = "templates"
//def githubAuthToken = ""

def fetch(addr, params = [:]) {
    def json = new JsonSlurper()
    if (githubAuthToken)
        return json.parse(addr.toURL().newReader(requestProperties: ["Authorization": "token ${auth}".toString(), "Accept": "application/json"]))
    else
        return json.parse(addr.toURL().newReader(requestProperties: ["Accept": "application/json"]))
}

def writeToFile(foldername, filename, toWrite) {
    def folder = new File(foldername)
    if( !folder.exists() ) {
        folder.mkdirs()
    }
    new File( folder, filename ).withWriter (toWrite)
}

def loadTemplate(templateName) {
    def engine = new groovy.text.GStringTemplateEngine()
    def file = new File(templateFolder, templateName)
    def pattern2 = ~'(\\<\\%(?:[^\\%]|(?:\\%[^\\>]))*\\%\\>)|([\\$]+(?:(?:\\{[^\\}]*\\})|(?:[^\\s]\\s+)))'
    def replaced = file.text.replaceAll(pattern2) {
        _, first,  inner -> if (inner) "<%=\'$inner\'%>" else first
    }
    return engine.createTemplate(replaced)
}

def createProject(repositoryName, outputFolder) {
    def binding = [repositoryName: repositoryName]
    writeToFile("$outputFolder/projects/", "${repositoryName}.setup" ) {
        it.write(loadTemplate('projectTemplate.setup.template').make(binding).toString())
    }

}

def createProjectCatalog(repositories, outputFolder) {
    def binding = [repositories: repositories]
    writeToFile("$outputFolder", "redirectable.projects.setup" ) {
        it.write(loadTemplate('redirectable.projects.setup.template').make(binding).toString())
    }
}

def repositories = fetch("https://api.github.com/orgs/PalladioSimulator/repos?per_page=100")
repositories.each { createProject(it.name, outputFolder) }
createProjectCatalog(repositories, outputFolder)
