package io.review360.assessor.plugins

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        jackson {
            setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
            setSerializationInclusion(JsonInclude.Include.NON_NULL)
        }
    }
}
