package com.creator_surge.network

import com.creator_surge.model.CreationRequest
import com.creator_surge.model.CreationResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object ApiClient {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }

    // Default 10.0.2.2 points to localhost host machine from Android Emulator
    private const val BASE_URL = "http://10.0.2.2:8080"

    suspend fun create(prompt: String, mediums: List<String> = listOf("image", "video", "game", "code", "docs")): CreationResult {
        return client.post("$BASE_URL/create") {
            contentType(ContentType.Application.Json)
            setBody(CreationRequest(prompt = prompt, mediums = mediums))
        }.body()
    }
}
