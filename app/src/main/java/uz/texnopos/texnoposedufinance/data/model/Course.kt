package uz.texnopos.texnoposedufinance.data.model

data class Course(
    val id: String = "",
    var name: String = "",
    var price: Int = 0,
    var duration: Int = 0,
    var groups: List<Group> = listOf()
)
