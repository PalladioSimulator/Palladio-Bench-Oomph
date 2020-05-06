import groovy.json.JsonSlurper
import org.palladiosimulator.bench.oomph.generator.ProjectCatalogGenerator
import org.palladiosimulator.bench.oomph.generator.ProjectGenerator

import static org.palladiosimulator.bench.oomph.generator.GenerationUtils.fetch

// The following parameters are injected by Maven
/*def outputFolderName
def deployPath
def templateFolderName
def githubAuthToken*/

def projects = fetch("https://api.github.com/orgs/PalladioSimulator/repos?per_page=100").name.unique(false).collect{it.toString()}.sort()
def projectSetupFiles = fetch("https://api.github.com/search/code?q=extension:setup+org:PalladioSimulator+path:releng?per_page=100").items
def projectTargetPlatforms = fetch("https://api.github.com/search/code?q=extension:target+org:PalladioSimulator+path:releng?per_page=100").items
def projectsWithoutSetupFile = projects - projectSetupFiles.repository.name.unique(false).collect{it.toString()}.sort()

def projectGenerator = new ProjectGenerator(outputFolderName: "$outputFolderName/projects", templateFolder: new File(templateFolderName))
def projectCatalogGenerator = new ProjectCatalogGenerator(outputFolderName: outputFolderName, templateFolder: new File(templateFolderName))

def outputFolderPrefix = new File(outputFolderName).path
def deployUrl = new URL(deployPath)

def generatedProjectSetups = projectsWithoutSetupFile
        .collect{project -> [repo: project,
                             filename: projectGenerator.createProject(project,
                                     {tp -> tp ? "https://raw.githubusercontent.com/$tp.repository.full_name/master/$tp.path": null}.call
                                             (projectTargetPlatforms.find {it.repository.name == project}))]}
        .collect{[repo: it.repo,
            url: new URL (deployUrl, (it.filename - outputFolderPrefix)
                    .replaceAll("\\\\", '/')
                    .replaceAll("^/?", '')
            ).toString()]
        }

def projectSetups = (generatedProjectSetups + projectSetupFiles.collect {
    [repo: it.repository.name, url: "https://raw.githubusercontent.com/$it.repository.full_name/master/$it.path"]
}).sort { a, b -> a.repo <=> b.repo }

projectCatalogGenerator.createProjectCatalog(projectSetups.url)

