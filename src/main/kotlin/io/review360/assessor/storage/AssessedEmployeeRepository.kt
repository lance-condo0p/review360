package io.review360.assessor.storage

import io.review360.assessor.model.Employee
import io.review360.assessor.model.SkillCode

data class AssessmentResult(
    val scorePerReviewer: MutableMap<String, Int>, // String = ReviewerEmail
) {
    fun getAverage(): Double {
        return scorePerReviewer.values.filter { it > 0 }.average()
    }
}

data class ReviewResults(
    val skills: MutableMap<SkillCode, AssessmentResult>,
)

/*
 * In-memory container for processed review forms
 */
object AssessedEmployeeRepository {
    private val assessedEmployees = mutableMapOf<Employee, ReviewResults>()

    fun allAssessedEmployees(): Map<Employee, ReviewResults> = assessedEmployees

    fun performAssessment(forms: List<ReviewForm>) {
        for (reviewForm in forms) {
            val reviewer = reviewForm.reviewerEmail
            val employee = EmployeesRepository.getEmployeeByEmail(reviewForm.employeeEmail)
            if (assessedEmployees.containsKey(employee)) {
                reviewForm.answers.map {
                    assessedEmployees[employee]!!.skills[it.code]!!.scorePerReviewer[reviewer]=it.points.weight
                }
            } else {
                assessedEmployees[employee!!] = ReviewResults(
                    skills = reviewForm.answers.associate { it.code to AssessmentResult(
                        scorePerReviewer = mutableMapOf(reviewer to it.points.weight)
                    ) }.toMutableMap()
                )
            }
        }
    }
}
