package uz.texnopos.texnoposedufinance.data.model

data class Payment(
    var id: String = "",
    var amount: Int = 0,
    var date: String = "",
    var createdDate: String = "",
    var participantId: String = "",
    var groupId: String = "",
    var courseId: String = ""
)