package uz.texnopos.texnoposedufinance.data.model.response

data class ReportResponse(
    var fromDate: Long = 0L,
    var toDate: Long = 0L,
    var incomeCategories: List<MyResponse> = listOf(),
    var expenseCategories: List<MyResponse> = listOf()
)