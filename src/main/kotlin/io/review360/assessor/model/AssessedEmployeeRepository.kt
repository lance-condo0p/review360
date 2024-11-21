package io.review360.assessor.model

data class AssessmentResult(
    val scorePerReviewer: MutableMap<String, Int>, // String = Reviewer
) {
    fun getAverage(): Double {
        return scorePerReviewer.values.average();
    }
}

data class ReviewResults(
    val skills: MutableMap<SkillCode, AssessmentResult>,
)

object AssessedEmployeeRepository {
    private val assessedEmployees = mutableMapOf<String, ReviewResults>() // String = Employee

    fun allAssessedEmployees(): Map<String, ReviewResults> = assessedEmployees

    fun performAssessment(forms: List<ReviewForm>) {
        for (reviewForm in forms) {
            val reviewer = reviewForm.reviewerEmail
            if (assessedEmployees.containsKey(reviewForm.employeeEmail)) {
                reviewForm.answers.map {
                    assessedEmployees[reviewForm.employeeEmail]!!.skills[it.code]!!.scorePerReviewer[reviewer]=it.points.weight
                }
            } else {
                assessedEmployees[reviewForm.employeeEmail] = ReviewResults(
                    skills = reviewForm.answers.associate { it.code to AssessmentResult(
                        scorePerReviewer = mutableMapOf(reviewer to it.points.weight)
                    ) }.toMutableMap()
                )
            }
        }
    }
}
