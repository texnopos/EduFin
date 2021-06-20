package uz.texnopos.texnoposedufinance.data.model.response

data class ExpenseRequest(
    var amount: Int = 0,
    var category: String = "",
    var date: Long = 0,
    var createdDate: Long = 0,
    var id: String = "",
    var note: String = "",
    var employeeId: String = "",
    var orgId: String = ""
)
