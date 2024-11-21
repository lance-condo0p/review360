package io.review360.assessor.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.review360.assessor.model.*

import io.review360.assessor.model.SkillCode

fun Application.configureRouting() {
    routing {
        route("/api/v1/forms") {
            post {
                val form = call.receive<ReviewForm>()
                if (FormsRepository.submitForm(form)) {
                    call.respond(HttpStatusCode.Created)
                } else {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
            get {
                call.respond(
                    FormsRepository.allForms()
                )
            }
            get("employees") {
                call.respond(
                    listOf(
                        Employee(
                            email = "ivan@mail.ru",
                            name = "Ivan",
                        ),
                        Employee(
                            email = "mark@mail.ru",
                            name = "Mark",
                        ),
                        Employee(
                            email = "fedor@ya.ru",
                            name = "Fedor",
                        ),
                    )
                )
            }
            get("questions") {
                val questions = ArrayList<Answer>()
                SkillCode.entries.toTypedArray().forEach {
                    questions.add(
                        Answer(
                            code = it,
                            description = it.description,
                        )
                    )
                }
                call.respond(questions)
            }
            get("marks") {
                val marks = ArrayList<Pair<Score, String>>()
                Score.entries.toTypedArray().forEach {
                    marks.add(
                        Pair(it, it.description)
                    )
                }
                call.respond(marks)
            }
        }
        route("/api/v1/admin") {
            post("download") {
                createExcel(
                    FormsRepository.allForms()
                )
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}
