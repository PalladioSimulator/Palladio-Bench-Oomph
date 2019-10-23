package org.palladiosimulator.bench.oomph.generator

import groovy.json.JsonSlurper
import groovy.text.GStringTemplateEngine

final class GenerationUtils {
    static final def SETUP_TEMPLATE_AWARE_TEMPLATE_SANITIZATION_PATTERN =
            ~'(\\<\\%(?:[^\\%]|(?:\\%[^\\>]))*\\%\\>)|([\\$]+(?:(?:\\{[^\\}]*\\})|(?:[^\\s]\\s+)))'

    static def fetch(String addr, params = [:]) {
        def json = new JsonSlurper()
        return json.parse(addr.toURL().newReader(requestProperties: ["Accept": "application/json"]))
    }

    static String sanitizeTemplateVariable(final String inner) {
        "<%=\'$inner\'%>"
    }

    static File writeToFile(String foldername, String filename, Closure toWrite) {
        def folder = new File(foldername)
        if (!folder.exists()) {
            folder.mkdirs()
        }
        def f = new File(folder, filename)
        f.withWriter(toWrite)
        return f
    }

    static def loadTemplate(File templateFolder, String templateName) {
        def engine = new GStringTemplateEngine()
        def file = new File(templateFolder, templateName)
        def replaced = file.text.replaceAll(SETUP_TEMPLATE_AWARE_TEMPLATE_SANITIZATION_PATTERN) {
            _, String first, String inner -> inner ? sanitizeTemplateVariable(inner) : first
        }
        return engine.createTemplate(replaced)
    }
}
