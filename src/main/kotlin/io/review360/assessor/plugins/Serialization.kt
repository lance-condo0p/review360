package io.review360.assessor.plugins

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.review360.assessor.plugins.JsonMapper.defaultMapper

object JsonMapper {
    // automatically installs the Kotlin module
    val defaultMapper: ObjectMapper = jacksonObjectMapper()

    init {
        defaultMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
        defaultMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
    }
}

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        register(ContentType.Application.Json, JacksonConverter(defaultMapper))
    }
}
