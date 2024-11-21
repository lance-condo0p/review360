package io.review360.assessor.storage

import io.review360.assessor.model.Employee

/*
 * In-memory container for employees review forms
 */
object EmployeesRepository {
    private val employees = setOf(
        Employee(
            email = "ivan@mail.ru",
            name = "John Smith",
        ),
        Employee(
            email = "mark@mail.ru",
            name = "Mark Axe",
        ),
        Employee(
            email = "fedor@ya.ru",
            name = "Mike Ox",
        ),
    )

    fun getEmployeeByEmail(email: String) = employees.findLast {
        it.email == email
    }

    fun allEmployees(): Set<Employee> = employees
}