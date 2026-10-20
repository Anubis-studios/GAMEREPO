package com.creator_surge.engines

import java.awt.Color
import java.awt.Font
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlinx.serialization.Serializable

@Serializable
data class ImageArtefact(
    val title: String,
    val url: String,
    val filePath: String,
    val width: Int,
    val height: Int
)

class ImageEngine {
    fun generate(prompt: String, outputDir: File, baseUrl: String): List<ImageArtefact> {
        val imagesDir = File(outputDir, "images").apply { mkdirs() }
        val fileName = "generated_${System.currentTimeMillis()}.png"
        val outputFile = File(imagesDir, fileName)

        val width = 800
        val height = 600
        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val g2d = image.createGraphics()

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2d.color = Color(30, 30, 46)
        g2d.fillRect(0, 0, width, height)

        g2d.color = Color(137, 180, 250)
        g2d.drawRoundRect(20, 20, width - 40, height - 40, 30, 30)

        g2d.color = Color(203, 166, 247)
        g2d.font = Font("SansSerif", Font.BOLD, 28)
        g2d.drawString("Creator Surge AI Image", 50, 80)

        g2d.color = Color(205, 214, 244)
        g2d.font = Font("SansSerif", Font.PLAIN, 20)
        val wrappedPrompt = if (prompt.length > 50) prompt.take(47) + "..." else prompt
        g2d.drawString("Prompt: $wrappedPrompt", 50, 140)

        g2d.color = Color(245, 194, 231)
        g2d.fillOval(250, 220, 300, 300)
        g2d.color = Color(148, 226, 213)
        g2d.fillOval(320, 270, 160, 160)

        g2d.dispose()

        ImageIO.write(image, "png", outputFile)

        val relativeUrl = "$baseUrl/outputs/images/$fileName"
        return listOf(
            ImageArtefact(
                title = "Generated Artwork for '$prompt'",
                url = relativeUrl,
                filePath = outputFile.absolutePath,
                width = width,
                height = height
            )
        )
    }
}
