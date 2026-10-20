package com.creator_surge.model

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
data class ImageArtefact(
    val title: String,
    val url: String,
    val filePath: String,
    val width: Int = 800,
    val height: Int = 600
)

@Serializable
data class VideoArtefact(
    val title: String,
    val url: String,
    val filePath: String,
    val durationSeconds: Int = 5,
    val mimeType: String = "video/mp4"
)

@Serializable
data class GameArtefact(
    val title: String,
    val url: String,
    val filePath: String,
    val description: String
)

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

@Serializable
data class DocsArtefact(
    val title: String,
    val url: String,
    val filePath: String,
    val markdownContent: String
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
