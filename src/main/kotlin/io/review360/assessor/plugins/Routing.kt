package io.review360.assessor.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.review360.assessor.model.*

import io.review360.assessor.model.SkillCode
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
                    EmployeesRepository.allEmployees()
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
        authenticate("auth-basic") {
            route("$URL_PREFIX/admin") {
                post("report/download") {
                    AssessedEmployeeRepository.performAssessment(FormsRepository.allForms())
                    createExcel(AssessedEmployeeRepository.allAssessedEmployees())
                    call.respond(HttpStatusCode.OK)
                }
                get("report") {
                    AssessedEmployeeRepository.performAssessment(FormsRepository.allForms())
                    call.respond(
                        AssessedEmployeeRepository.allAssessedEmployees()
                    )
                }
                get("forms") {
                    call.respond(
                        FormsRepository.allForms()
                    )
                }
            }
        }
        route("$URL_PREFIX/init") {
            post {
                val credentials = call.receive<Credentials>()
                if (SecretsVault.init(credentials.login, credentials.password))
                    call.respond(HttpStatusCode.OK)
                else
                    call.respond(HttpStatusCode.MethodNotAllowed)
            }
        }
    }
}
