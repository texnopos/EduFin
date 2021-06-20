package uz.texnopos.texnoposedufinance.data.model.response

data class IncomeRequest(
    var amount: Int = 0,
    var category: String = "",
    var date: Long = 0,
    var createdDate: Long = 0,
    var id: String = "",
    var note: String = "",
    var orgId: String = "",
    var courseId: String = "",
    var groupId: String = "",
    var participantId: String = ""
)
