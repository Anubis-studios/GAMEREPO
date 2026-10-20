package com.creator_surge.router

import com.creator_surge.engines.CodeArtefact
import com.creator_surge.engines.CodeEngine
import com.creator_surge.engines.DocsArtefact
import com.creator_surge.engines.DocsEngine
import com.creator_surge.engines.GameArtefact
import com.creator_surge.engines.GameEngine
import com.creator_surge.engines.ImageArtefact
import com.creator_surge.engines.ImageEngine
import com.creator_surge.engines.VideoArtefact
import com.creator_surge.engines.VideoEngine
import java.io.File
import kotlinx.serialization.Serializable

@Serializable
data class CreationRequest(
    val prompt: String,
    val mediums: List<String> = listOf("image", "video", "game", "code", "docs")
)

@Serializable
data class Overview(
    val prompt: String,
    val status: String,
    val totalArtefacts: Int,
    val timestamp: Long
)

@Serializable
data class CreationResult(
    val overview: Overview,
    val images: List<ImageArtefact> = emptyList(),
    val videos: List<VideoArtefact> = emptyList(),
    val game: List<GameArtefact> = emptyList(),
    val code: List<CodeArtefact> = emptyList(),
    val docs: List<DocsArtefact> = emptyList()
)

class CreationRouter(
    private val outputDir: File,
    private val baseUrl: String = "http://10.0.2.2:8080"
) {
    private val imageEngine = ImageEngine()
    private val videoEngine = VideoEngine()
    private val gameEngine = GameEngine()
    private val codeEngine = CodeEngine()
    private val docsEngine = DocsEngine()

    fun routeCreation(request: CreationRequest): CreationResult {
        val prompt = request.prompt
        val mediums = request.mediums.map { it.lowercase() }

        val images = if ("image" in mediums || mediums.isEmpty()) imageEngine.generate(prompt, outputDir, baseUrl) else emptyList()
        val videos = if ("video" in mediums || mediums.isEmpty()) videoEngine.generate(prompt, outputDir, baseUrl) else emptyList()
        val games = if ("game" in mediums || mediums.isEmpty()) gameEngine.generate(prompt, outputDir, baseUrl) else emptyList()
        val code = if ("code" in mediums || mediums.isEmpty()) codeEngine.generate(prompt, outputDir, baseUrl) else emptyList()
        val docs = if ("docs" in mediums || mediums.isEmpty()) docsEngine.generate(prompt, outputDir, baseUrl) else emptyList()

        val total = images.size + videos.size + games.size + code.size + docs.size

        val overview = Overview(
            prompt = prompt,
            status = "SUCCESS",
            totalArtefacts = total,
            timestamp = System.currentTimeMillis()
        )

        return CreationResult(
            overview = overview,
            images = images,
            videos = videos,
            game = games,
            code = code,
            docs = docs
        )
    }
}
