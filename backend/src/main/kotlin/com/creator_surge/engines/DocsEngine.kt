package com.creator_surge.engines

import java.io.File
import kotlinx.serialization.Serializable

@Serializable
data class DocsArtefact(
    val title: String,
    val url: String,
    val filePath: String,
    val markdownContent: String
)

class DocsEngine {
    fun generate(prompt: String, outputDir: File, baseUrl: String): List<DocsArtefact> {
        val docsDir = File(outputDir, "docs").apply { mkdirs() }
        val fileName = "DOCS_${System.currentTimeMillis()}.md"
        val outputFile = File(docsDir, fileName)

        val markdown = """
            # Creator Surge Documentation

            ## Request Summary
            - **Prompt**: $prompt
            - **Timestamp**: ${java.util.Date()}
            - **Status**: Successfully Generated

            ## Executive Overview
            Creator Surge Super-Agent has parsed your request and generated multimodal artefacts across:
            - 🎨 **Images**: High-resolution generated canvas art.
            - 📹 **Video**: Synthesized video stream clip.
            - 🎮 **Game**: Fully functional web mini-game.
            - 💻 **Code**: Clean Kotlin architecture source code.
            - 📝 **Docs**: System architecture & usage documentation.

            ## Integration & Usage Guide
            1. Access generated static assets via Ktor HTTP endpoints.
            2. Run embedded interactive games within WebViews on Android.
            3. Preview video streams using ExoPlayer / Media3.
        """.trimIndent()

        outputFile.writeText(markdown)

        val relativeUrl = "$baseUrl/outputs/docs/$fileName"
        return listOf(
            DocsArtefact(
                title = "Creator Surge Specification",
                url = relativeUrl,
                filePath = outputFile.absolutePath,
                markdownContent = markdown
            )
        )
    }
}
