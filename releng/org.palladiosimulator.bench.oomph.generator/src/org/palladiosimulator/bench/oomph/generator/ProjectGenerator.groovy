package org.palladiosimulator.bench.oomph.generator

import org.palladiosimulator.maven.tychotprefresh.tp.model.TargetPlatformFile
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
        //The following target platforms are temporarily hardcoded
        //FIXME:It would be more proper to evaluate the dynamic tp generated during the maven build of the project
        def locations = [retrieveTPFile(targetPlatformURL),
         retrieveTPFile("https://raw.githubusercontent.com/MDSD-Tools/Maven-Build-TargetPlatforms/master/targetPlatforms/eclipse-modeling-2019-06.target"),
         retrieveTPFile("https://raw.githubusercontent.com/PalladioSimulator/Palladio-Build-MavenTP/master/license.target")]
            .findAll {it.isPresent()}
            .collect {it.get()}
            .collectMany {it.locations.findAll { it.filter ? it.filter == "nightly" : true }}


        [sites: locations.collect { it.repositoryLocation },
         units: locations.collectMany {
             it.units.collect { it.id }
         }]

    }

    private Optional<TargetPlatformFile> retrieveTPFile(String platformURL) {
        File tf = File.createTempFile("targetplatform", ".tmp.target")
        tf << new URL(platformURL).getText()
        def tp = TargetPlatformParser.parse(tf)
        tf.delete()
        tp
    }
}