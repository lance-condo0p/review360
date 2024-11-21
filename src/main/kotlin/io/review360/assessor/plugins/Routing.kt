package io.review360.assessor.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.review360.assessor.model.*
import io.review360.assessor.storage.*

const val URL_VERSION = "/v1"
const val URL_NAME = "/api"
const val URL_PREFIX = "$URL_NAME$URL_VERSION"

fun Application.configureRouting() {
    routing {
        route("$URL_PREFIX/forms") {
            post {
                val form = call.receive<ReviewForm>()
                when (FormsRepository.submitForm(form)) {
                    SubmissionResult.OK -> call.respond(SubmissionResult.OK.statusCode, SubmissionResult.OK.result)
                    SubmissionResult.InvalidEmployee -> call.respond(SubmissionResult.InvalidEmployee.statusCode, SubmissionResult.InvalidEmployee.result)
                    SubmissionResult.InvalidSkill -> call.respond(SubmissionResult.InvalidSkill.statusCode, SubmissionResult.InvalidSkill.result)
                    SubmissionResult.Duplicate -> call.respond(SubmissionResult.Duplicate.statusCode, SubmissionResult.Duplicate.result)
                }
            }
            get("employees") {
                call.respond(
                    EmployeesRepository.getAll()
                )
            }
            get("questions") {
                val questions = ArrayList<Question>()
                SkillsRepository.getAll().forEach {
                    questions.add(
                        Question(
                            code = it.code,
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
        authenticate("auth-basic") {
            route("$URL_PREFIX/admin") {
                post("report/download") {
                    AssessedEmployeeRepository.performAssessment(FormsRepository.getAll())
                    createExcel(AssessedEmployeeRepository.getAll())
                    call.respond(HttpStatusCode.OK)
                }
                get("report") {
                    AssessedEmployeeRepository.performAssessment(FormsRepository.getAll())
                    call.respond(
                        AssessedEmployeeRepository.getAll()
                    )
                }
                get("forms") {
                    call.respond(
                        FormsRepository.getAll()
                    )
                }
            }
        }
        route("$URL_PREFIX/init") {
            post {
                val credentials = call.receive<UserPasswordCredential>()
                if (SecretsVault.init(credentials)) {
                    EmployeesRepository.init()
                    SkillsRepository.init()
                    // ToDo: validate forms agains loaded employees and skills to avoid "broken links"
                    FormsRepository.init()
                    call.respond(HttpStatusCode.OK)
                } else
                    call.respond(HttpStatusCode.MethodNotAllowed)
            }
        }
    }
}
