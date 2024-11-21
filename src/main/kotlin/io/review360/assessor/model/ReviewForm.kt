package io.review360.assessor.model

enum class SkillType {
    HardSkill,
    SoftSkill,
}

data class Reviewer(
    val email: String,
)

data class Employee(
    val email: String,
    val name: String,
)

data class Skill(
    val code: Skills,
    val type: SkillType = code.type,
    val description: String = code.description,
    val points: Mark? = null,
)

data class ReviewForm(
    val reviewerEmail: String,
    val employeeEmail: String,
    val answers: List<Skill>,
//    val softSkills: List<Skill<SoftSkillTypes>>,
//    val hardSkills: List<Skill<HardSkillTypes>>,
)

