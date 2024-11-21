package io.review360.assessor.model

enum class SkillCode(
    val description: String,
    val type: SkillType,
) {
    SoftType1("Employee has the ability to talk", SkillType.GeneralSoft),
    SoftType2("Employee has the ability to walk", SkillType.GeneralSoft),
    SoftType3("Employee has the ability to get", SkillType.GeneralSoft),
    HardType1("Employee knows Java programming", SkillType.ProgrammingHard),
    HardType2("Employee knows Git", SkillType.ProgrammingHard),
    HardType3("Employee has Ansible knowledge", SkillType.ProgrammingHard),
    HardType4("Employee has SQL knowledge", SkillType.ProgrammingHard),
}