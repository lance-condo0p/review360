package io.review360.assessor.plugins

import io.review360.assessor.model.Employee
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File

fun createExcel(assessedEmployees: Map<Employee, ReviewResults>) {
    val workbook = XSSFWorkbook()
    for(employee in assessedEmployees) {
        val workSheet = workbook.createSheet("${employee.key.name} (${employee.key.email})")

        val cellStyle = workbook.createCellStyle()
        cellStyle.fillForegroundColor = IndexedColors.GREY_25_PERCENT.getIndex()
        cellStyle.fillPattern = FillPatternType.SOLID_FOREGROUND

        var rowPointer = 0
        var columnPointer = 0

        val headRow = workSheet.createRow(rowPointer++)
        val cell = headRow.createCell(columnPointer++)
        cell.setCellValue("Skill Code")
        cell.cellStyle = cellStyle
        val cell2 = headRow.createCell(columnPointer++)
        cell2.setCellValue("Description")
        cell2.cellStyle = cellStyle
        val cell3 = headRow.createCell(columnPointer++)
        cell3.setCellValue("Type")
        cell3.cellStyle = cellStyle
        val cell4 = headRow.createCell(columnPointer)
        cell4.setCellValue("Score (average)")
        cell4.cellStyle = cellStyle

        for (skill in employee.value.skills) {
            columnPointer = 0

            val row = workSheet.createRow(rowPointer++)

            row.createCell(columnPointer++).setCellValue(skill.key.code)
            row.createCell(columnPointer++).setCellValue(skill.key.description)
            row.createCell(columnPointer++).setCellValue(skill.key.type.name)
            row.createCell(columnPointer++).setCellValue(skill.value.getAverage())

            for (reviewer in skill.value.scorePerReviewer) {
                headRow.createCell(columnPointer).setCellValue("Score (${reviewer.key})")
                row.createCell(columnPointer++).setCellValue(reviewer.value.toDouble())
            }
        }
    }

    try {
        val file = File("db/test.xlsx")
        val stream = file.outputStream()
        workbook.write(stream)
        workbook.close()
        stream.close()
    } catch (e: Exception) {
        println(e.message)
    }
}
