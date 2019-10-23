package org.palladiosimulator.bench.oomph.generator

import static org.palladiosimulator.bench.oomph.generator.GenerationUtils.loadTemplate
import static org.palladiosimulator.bench.oomph.generator.GenerationUtils.writeToFile

class ProjectCatalogGenerator {
    File templateFolder
    String outputFolderName

    def createProjectCatalog(List<String> setupLocations) {
        def binding = [repositories: setupLocations]
        writeToFile("$outputFolderName", "redirectable.projects.setup") {
            it.write(loadTemplate(templateFolder, 'redirectable.projects.setup.template').make(binding).toString())
        }
    }
}