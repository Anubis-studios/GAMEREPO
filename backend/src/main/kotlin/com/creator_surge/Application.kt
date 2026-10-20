package com.creator_surge

import com.creator_surge.router.CreationRequest
import com.creator_surge.router.CreationRouter
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.http.content.staticFiles
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.request.header
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import java.io.File
import kotlinx.serialization.json.Json

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val outputsDir = File("outputs").apply { mkdirs() }

    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }

    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Get)
        allowHeader(HttpHeaders.ContentType)
        anyHost()
    }

    val router = CreationRouter(outputsDir)

    routing {
        get("/") {
            call.respond(mapOf("status" to "Creator Surge Ktor Backend Running"))
        }

        staticFiles("/outputs", outputsDir)

        post("/create") {
            try {
                val request = call.receive<CreationRequest>()
                val hostHeader = call.request.header("Host") ?: "10.0.2.2:8080"
                val scheme = if (call.request.header("X-Forwarded-Proto") == "https") "https" else "http"
                val baseUrl = "$scheme://$hostHeader"

                val result = router.routeCreation(request, baseUrl)
                call.respond(HttpStatusCode.OK, result)
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to (e.message ?: "Unknown error occurred"))
                )
            }
        }
    }
}
