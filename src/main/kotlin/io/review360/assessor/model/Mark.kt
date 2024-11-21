package io.review360.assessor.model

enum class Mark(
    val description: String
) {
    Nope("0 - I don't know"),
    Low("1 - Absolutely disagree"),
    Medium("2 - Probably yes"),
    High("3 - Absolutely agree"),
    Highest("4 - More than agree"),
}