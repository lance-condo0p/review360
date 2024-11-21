package io.review360.assessor.model

enum class Score(
    val description: String,
    val weight: Int,
) {
    Nope("I don't know", 0),
    Low("Absolutely disagree", 1),
    Medium("Probably yes", 2),
    High("Absolutely agree", 3),
    Highest("More than agree", 4),
}