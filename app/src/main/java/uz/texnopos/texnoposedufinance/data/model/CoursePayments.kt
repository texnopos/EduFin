package uz.texnopos.texnoposedufinance.data.model

data class CoursePayments(
    var id: String = "",
    var amount: Int = 0,
    var date: Long = 0L,
    var createdDate: Long = 0L,
    var participantId: String = "",
    var groupId: String = "",
    var courseId: String = "",
    var orgId: String = "",
    var category: String = ""
)