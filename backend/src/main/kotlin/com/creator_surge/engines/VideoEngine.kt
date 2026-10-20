package com.creator_surge.engines

import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer
import kotlinx.serialization.Serializable

@Serializable
data class VideoArtefact(
    val title: String,
    val url: String,
    val filePath: String,
    val durationSeconds: Int,
    val mimeType: String
)

class VideoEngine {
    fun generate(prompt: String, outputDir: File, baseUrl: String): List<VideoArtefact> {
        val videosDir = File(outputDir, "videos").apply { mkdirs() }
        val fileName = "generated_${System.currentTimeMillis()}.mp4"
        val outputFile = File(videosDir, fileName)

        // Generate a minimal valid MP4 container file structure
        val mp4Bytes = createMinimalMp4()
        FileOutputStream(outputFile).use { it.write(mp4Bytes) }

        val relativeUrl = "$baseUrl/outputs/videos/$fileName"
        return listOf(
            VideoArtefact(
                title = "Video Clip: $prompt",
                url = relativeUrl,
                filePath = outputFile.absolutePath,
                durationSeconds = 5,
                mimeType = "video/mp4"
            )
        )
    }

    private fun createMinimalMp4(): ByteArray {
        // Minimal ftyp box + mdat box for a dummy MP4 payload
        val ftyp = byteArrayOf(
            0x00, 0x00, 0x00, 0x20, // box length = 32
            'f'.code.toByte(), 't'.code.toByte(), 'y'.code.toByte(), 'p'.code.toByte(),
            'i'.code.toByte(), 's'.code.toByte(), 'o'.code.toByte(), 'm'.code.toByte(),
            0x00, 0x00, 0x02, 0x00,
            'i'.code.toByte(), 's'.code.toByte(), 'o'.code.toByte(), 'm'.code.toByte(),
            'i'.code.toByte(), 's'.code.toByte(), 'o'.code.toByte(), '2'.code.toByte(),
            'a'.code.toByte(), 'v'.code.toByte(), 'c'.code.toByte(), '1'.code.toByte(),
            'm'.code.toByte(), 'p'.code.toByte(), '4'.code.toByte(), '1'.code.toByte()
        )
        val mdatData = "CREATOR_SURGE_DUMMY_VIDEO_STREAM".toByteArray(Charsets.UTF_8)
        val mdatHeader = ByteBuffer.allocate(8).apply {
            putInt(8 + mdatData.size)
            put('m'.code.toByte())
            put('d'.code.toByte())
            put('a'.code.toByte())
            put('t'.code.toByte())
        }.array()

        return ftyp + mdatHeader + mdatData
    }
}
