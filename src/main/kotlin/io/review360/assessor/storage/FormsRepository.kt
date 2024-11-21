package io.review360.assessor.storage

import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.util.logging.*
import io.review360.assessor.model.ReviewForm
import io.review360.assessor.model.SubmissionResult
import io.review360.assessor.plugins.JsonMapper.defaultMapper
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

/*
 * In-memory container for submitted review forms
 */
data object FormsRepository {
    private val submittedForms = mutableListOf<ReviewForm>()
    private val LOGGER = KtorSimpleLogger("io.review360.assessor.storage.FormsRepository")

    private fun getDuplicates(reviewerEmail: String, employeeEmail: String) = submittedForms.find {
        it.reviewerEmail == reviewerEmail && it.employeeEmail == employeeEmail
    }

    fun init() {
        val file = File("db/ReviewForms.json")
        if (file.exists()) {
            val jsonString = file.readText(Charsets.UTF_8)
            submittedForms.addAll(defaultMapper.readValue<List<ReviewForm>>(jsonString))
            LOGGER.trace("Loading review forms...")
            if (LOGGER.isTraceEnabled)
                for (form in submittedForms)
                    LOGGER.trace("- loaded: {}", form)
        } else {
            LOGGER.trace("Loading review forms skipped because no stored form found.")
        }
    }

    // ToDo: implement a less greedy algorithm as now to rewrites the entire file
    private fun saveToDB() {
        val file = File("db/ReviewForms.json")
        val output = BufferedWriter(FileWriter(file))
        defaultMapper.writeValue(output, submittedForms)
//        output.write(submittedForms.toString())
        output.close()
    }

    fun getAll(): List<ReviewForm> = submittedForms

    fun submitForm(form: ReviewForm): SubmissionResult {
        if (EmployeesRepository.getEmployeeByEmail(form.employeeEmail) == null) {
            return SubmissionResult.InvalidEmployee
        }
        // ToDo: check skills agains dictionary
//        if (form.answers.forEach { it.code instanceOf SkillCode } ) {
//            return SubmissionResult.InvalidSkill
//        }
        if (getDuplicates(form.reviewerEmail, form.employeeEmail) != null) {
            return SubmissionResult.Duplicate
        }
        submittedForms.add(form)
        saveToDB()
        return SubmissionResult.OK
    }
}