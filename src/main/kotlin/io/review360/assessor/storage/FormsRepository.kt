package io.review360.assessor.storage

import io.ktor.http.*
import io.review360.assessor.model.Score
import io.review360.assessor.model.SkillCode

data class Answer(
    val code: SkillCode,
    val description: String = code.description,
    val points: Score = Score.None,
)

data class ReviewForm(
    val reviewerEmail: String,
    val employeeEmail: String,
    val answers: Set<Answer>,
)

data class ProcessingMessage(
    val message: String,
)

enum class SubmissionResult(
    val statusCode: HttpStatusCode,
    val result: ProcessingMessage,
) {
    OK(HttpStatusCode.Created, ProcessingMessage("Submitted successfully")),
    InvalidEmployee(HttpStatusCode.NotAcceptable, ProcessingMessage("Employee unknown!")),
    InvalidSkill(HttpStatusCode.NotAcceptable, ProcessingMessage("Skill unknown!")),
    Duplicate(HttpStatusCode.Conflict, ProcessingMessage("Review Form has been submitted already!")),
}

/*
 * In-memory container for submitted review forms
 */
object FormsRepository {
    private val submittedForms = mutableListOf<ReviewForm>()

    fun allForms(): List<ReviewForm> = submittedForms

    private fun getDuplicates(reviewerEmail: String, employeeEmail: String) = submittedForms.find {
        it.reviewerEmail == reviewerEmail && it.employeeEmail == employeeEmail
    }

    fun submitForm(form: ReviewForm): SubmissionResult {
        if (EmployeesRepository.getEmployeeByEmail(form.employeeEmail) == null) {
            return SubmissionResult.InvalidEmployee
        }
        // ToDo: check skills agains dictionary
//        if (form.answers.forEach { it.code instanceOf SkillCode } ) {
//            return SubmissionResult.InvalidSkill
//        }
        if (getDuplicates(form.reviewerEmail, form.employeeEmail) != null) {
            return SubmissionResult.Duplicate
        }
        submittedForms.add(form)
        return SubmissionResult.OK
    }
}