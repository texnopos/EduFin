package uz.texnopos.texnoposedufinance.data.model

data class Report (
    var id: String = "",
    var amount: Int = 0,
    var createdDate: Long = 0,
    var date: Long = 0,
    var category: String = "",
    var note: String = "")