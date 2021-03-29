package uz.texnopos.texnoposedufinance.data.model

data class Course(
        val id: String = "",
        val orgId: String = "",
        var name: String = "",
        var price: Double = 0.0,
        var duration: Int = 0
)