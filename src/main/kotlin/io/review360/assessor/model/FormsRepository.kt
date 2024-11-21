package io.review360.assessor.model

object FormsRepository {
    private val submittedForms = mutableListOf<ReviewForm>()

    fun allForms(): List<ReviewForm> = submittedForms

    private fun getDuplicates(reviewerEmail: String, employeeEmail: String) = submittedForms.find {
        it.reviewerEmail == reviewerEmail && it.employeeEmail == employeeEmail
    }

    fun submitForm(form: ReviewForm): Boolean {
        if (getDuplicates(form.reviewerEmail, form.employeeEmail) != null) {
            return false
        }
        submittedForms.add(form)
        return true
    }
}