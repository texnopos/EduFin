package uz.texnopos.texnoposedufinance.data.model

data class Student(
    var id: String = "",
    var groupId: String = "",
    var courseId: String = "",
    var orgId: String = "",
    var name: String = "",
    var phone: List<String> = listOf(),
    var interested: String = "",
    var passport: String = "",
    var birthDate: String = "",
    var address: String = "",
    var contractNum: Int = 0
)