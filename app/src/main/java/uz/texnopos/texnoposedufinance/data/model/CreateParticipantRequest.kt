package uz.texnopos.texnoposedufinance.data.model

data class CreateParticipantRequest(
    var orgId: String = "",
    var courseId: String = "",
    var groupId: String = "",
    var id: String = "",
    var name: String = "",
    var passport: String = "",
    var contract: Int = 0,
    var birthDate: Long = 0L,
    var createdDate: Long = 0L,
    var phone: List<String> = listOf(),
    var address: String = "",
    var course: String = ""
)