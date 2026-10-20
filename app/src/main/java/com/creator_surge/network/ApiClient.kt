package com.creator_surge.network

import com.creator_surge.model.CreationRequest
import com.creator_surge.model.CreationResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
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
        install(HttpTimeout) {
            requestTimeoutMillis = 60_000L
            connectTimeoutMillis = 15_000L
            socketTimeoutMillis = 60_000L
        }
    }

    suspend fun create(
        prompt: String,
        baseUrl: String = "http://10.0.2.2:8080",
        mediums: List<String> = listOf("image", "video", "game", "code", "docs")
    ): CreationResult {
        val cleanUrl = baseUrl.trim().removeSuffix("/")
        val endpoint = if (cleanUrl.startsWith("http://") || cleanUrl.startsWith("https://")) {
            "$cleanUrl/create"
        } else {
            "http://$cleanUrl/create"
        }

        return client.post(endpoint) {
            contentType(ContentType.Application.Json)
            setBody(CreationRequest(prompt = prompt, mediums = mediums))
        }.body()
    }
}
