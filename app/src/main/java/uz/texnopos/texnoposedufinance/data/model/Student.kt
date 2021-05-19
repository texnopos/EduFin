package uz.texnopos.texnoposedufinance.data.model

data class Student(
    var id: String = "",
    var name: String = "",
    var phone: List<String> = listOf(),
    var course: String = "",
    var passport: String = "",
    var birthDate: Long = 0,
    var address: String = ""
)