package uz.texnopos.texnoposedufinance.data.model.response

data class SalaryResponse(
    var fromDate: Long = 0L,
    var toDate: Long = 0L,
    var employeeSalary: List<EmployeeResponse> = listOf()
)