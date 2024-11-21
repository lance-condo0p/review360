package io.review360.assessor.storage

import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.util.logging.*
import io.review360.assessor.model.Employee
import io.review360.assessor.plugins.JsonMapper.defaultMapper
import java.io.File

/*
 * In-memory container for employees list
 */
data object EmployeesRepository: FileRepository<Employee> {
    private val employees = mutableSetOf<Employee>()
    private val LOGGER = KtorSimpleLogger("io.review360.assessor.storage.EmployeesRepository")

    override fun init() {
        val jsonString = File("db/Employees.json").readText(Charsets.UTF_8)
        employees.addAll(defaultMapper.readValue<Set<Employee>>(jsonString))
        LOGGER.trace("Loading employees...")
        if (LOGGER.isTraceEnabled)
            for (employee in employees)
                LOGGER.trace("- loaded: {}", employee)
    }

    fun getEmployeeByEmail(email: String) = employees.findLast {
        it.email == email
    }

    override fun getAll(): Set<Employee> = employees
}