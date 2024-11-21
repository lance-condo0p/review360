package io.review360.assessor.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.request.*

fun Application.configureLogging() {
    install(CallLogging) {
        format { call ->
            val status = call.response.status()
            val url = call.request.uri
            val httpMethod = call.request.httpMethod.value
            val userAgent = call.request.headers["User-Agent"]
            "Status: $status, HTTP method: $httpMethod, Endpoint: $url, User agent: $userAgent"
        }
    }
}