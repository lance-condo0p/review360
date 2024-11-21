package io.review360.assessor.model

enum class Skills(
    val description: String,
    val type: SkillType,
) {
    SoftType1("The ability to talk", SkillType.SoftSkill),
    SoftType2("The ability to walk", SkillType.SoftSkill),
    SoftType3("The ability to get", SkillType.SoftSkill),
    HardType1("Java programming", SkillType.HardSkill),
    HardType2("Ansible knowledge", SkillType.HardSkill),
    HardType3("SQL knowledge", SkillType.HardSkill),
}