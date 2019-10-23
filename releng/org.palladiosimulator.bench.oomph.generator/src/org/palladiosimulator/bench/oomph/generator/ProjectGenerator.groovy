package org.palladiosimulator.bench.oomph.generator
import static org.palladiosimulator.bench.oomph.generator.GenerationUtils.writeToFile
import static org.palladiosimulator.bench.oomph.generator.GenerationUtils.loadTemplate

class ProjectGenerator {
    File templateFolder
    String outputFolderName

    String createProject(repositoryName, String targetPlatformURL) {
        def binding = [repositoryName: repositoryName,
                       p2dependencies: targetPlatformURL ? determineDependencies(targetPlatformURL) : []]
        println "$repositoryName: $targetPlatformURL"
        writeToFile("$outputFolderName", "${repositoryName}.setup") {
            it.write(loadTemplate(templateFolder, 'projectTemplate.setup.template').make(binding).toString())
        }.path
    }

    protected List determineDependencies(String targetPlatformURL) {
        []
    }
}