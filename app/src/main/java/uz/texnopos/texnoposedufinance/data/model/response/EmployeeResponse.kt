package uz.texnopos.texnoposedufinance.data.model.response

data class EmployeeResponse(
    var id: String = "",
    var name: String = "",
    var phone: String = "",
    var username: String = "",
    var password: String = "",
    var salary: String = "",
    var mySalary: List<ExpenseRequest> = listOf()
)
