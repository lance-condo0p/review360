package io.review360.assessor.model

data class Employee(
    val email: String,
    val name: String,
)

data class Answer(
    val code: SkillCode,
    val description: String = code.description,
    val points: Score = Score.Nope,
)

data class ReviewForm(
    val reviewerEmail: String,
    val employeeEmail: String,
    val answers: Set<Answer>,
)

