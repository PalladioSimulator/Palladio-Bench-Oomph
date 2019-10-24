package org.palladiosimulator.bench.oomph.generator

import org.palladiosimulator.maven.tychotprefresh.tp.parser.TargetPlatformParser

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

    protected Map determineDependencies(String targetPlatformURL) {
        File tf = File.createTempFile("targetplatform", ".tmp.target")
        tf << new URL(targetPlatformURL).getText()
        TargetPlatformParser.parse(tf).map {
            def filteredLocations = it.locations.findAll { it.filter ? it.filter == "nightly" : true }
            [sites: filteredLocations.collect { it.repositoryLocation },
             units: filteredLocations.collectMany {
                 it.units.collect { it.id }
             }]
        }.orElse([:])
    }
}