package io.review360.assessor.plugins

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.thymeleaf.Thymeleaf
import io.ktor.server.thymeleaf.ThymeleafContent
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver

fun Application.configureTemplating() {
    install(Thymeleaf) {
        setTemplateResolver(ClassLoaderTemplateResolver().apply {
            prefix = "templates/thymeleaf/"
            suffix = ".html"
            characterEncoding = "utf-8"
        })
    }
    routing {
        route("/forms") {
            post {
                val formContent = call.receiveParameters()
                val reviewerEmail = formContent["email"]?: ""
                call.respond(
                    ThymeleafContent("review360", mapOf("reviewerEmail" to reviewerEmail))
                )
            }
        }
        staticResources("/", "static")
    }
}