package uz.texnopos.texnoposedufinance.data.model
data class SendParticipantDataRequest(
    var id: String = "",
    var studentId: String = "",
    var groupId: String = "",
    var courseId: String = "",
    var orgId: String = "",
    var passport: String = "",
    var contract: Int = 0,
    var phone: List<String> = listOf()
)