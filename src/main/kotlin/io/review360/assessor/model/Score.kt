package io.review360.assessor.model

enum class Score(
    val description: String,
    val weight: Int,
) {
    None("0 - I don't know", 0),
    Low("1 - Absolutely disagree", 1),
    Medium("2 - Probably yes", 2),
    High("3 - Absolutely agree", 3),
    Highest("4 - More than agree", 4),
}