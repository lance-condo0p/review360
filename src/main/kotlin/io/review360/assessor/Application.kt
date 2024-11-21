package io.review360.assessor

import io.review360.assessor.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureDatabases()
    configureTemplating()
    configureSecurity()
    configureRouting()
    configureLogging()
}
