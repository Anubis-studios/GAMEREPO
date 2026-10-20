package com.creator_surge.engines

import java.io.File
import kotlinx.serialization.Serializable

@Serializable
data class CodeFile(
    val name: String,
    val relativePath: String,
    val content: String,
    val language: String
)

@Serializable
data class CodeArtefact(
    val title: String,
    val rootDir: String,
    val files: List<CodeFile>
)

class CodeEngine {
    fun generate(prompt: String, outputDir: File, baseUrl: String): List<CodeArtefact> {
        val codeDir = File(outputDir, "code/generated_project").apply { mkdirs() }
        val safePrompt = prompt.replace("\"", "\\\"").replace("\n", " ")

        val appFile = File(codeDir, "MainApp.kt")
        val appContent = """
            package com.creator_surge.generated

            /**
             * Generated Module for prompt: $safePrompt
             */
            fun main() {
                println("Executing Surge Generated Engine...")
                val engine = SurgeEngine(name = "$safePrompt")
                engine.run()
            }

            class SurgeEngine(val name: String) {
                fun run() {
                    println("SurgeEngine '${'$'}name' running successfully!")
                }
            }
        """.trimIndent()
        appFile.writeText(appContent)

        val modelFile = File(codeDir, "SurgeModel.kt")
        val modelContent = """
            package com.creator_surge.generated

            data class SurgeModel(
                val id: String,
                val prompt: String,
                val createdAt: Long = System.currentTimeMillis()
            )
        """.trimIndent()
        modelFile.writeText(modelContent)

        return listOf(
            CodeArtefact(
                title = "Generated Kotlin Module",
                rootDir = codeDir.absolutePath,
                files = listOf(
                    CodeFile("MainApp.kt", "code/generated_project/MainApp.kt", appContent, "kotlin"),
                    CodeFile("SurgeModel.kt", "code/generated_project/SurgeModel.kt", modelContent, "kotlin")
                )
            )
        )
    }
}
