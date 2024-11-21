package io.review360.assessor.model

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import io.ktor.http.*
import io.review360.assessor.storage.SkillsRepository

object SkillDeserializer: JsonDeserializer<Skill>() {
    override fun deserialize(parser: JsonParser, context: DeserializationContext): Skill {
        if (parser.isExpectedStartObjectToken) {
            return parser.readValueAs(Skill::class.java)
        } else {
            return SkillsRepository.getByCode(parser.text)!!
        }
    }
}

// ToDo: revise this data class to get rid of custom Deserializer
data class Answer(
    @JsonDeserialize(using = SkillDeserializer::class)
    val skill: Skill,
    val points: Score = Score.None,
)

//ToDo: remove points attribute
data class Question(
    val code: String, // Skill.code
    val description: String, // Skill.description
    val points: Score = Score.None,
)

data class ReviewForm(
    val reviewerEmail: String,
    val employeeEmail: String,
    val answers: Set<Answer>,
)

data class ProcessingMessage(
    val message: String,
)

enum class SubmissionResult(
    val statusCode: HttpStatusCode,
    val result: ProcessingMessage,
) {
    OK(HttpStatusCode.Created, ProcessingMessage("Submitted successfully")),
    InvalidEmployee(HttpStatusCode.NotAcceptable, ProcessingMessage("Employee unknown!")),
    InvalidSkill(HttpStatusCode.NotAcceptable, ProcessingMessage("Skill unknown!")),
    Duplicate(HttpStatusCode.Conflict, ProcessingMessage("Review Form has been submitted already!")),
}