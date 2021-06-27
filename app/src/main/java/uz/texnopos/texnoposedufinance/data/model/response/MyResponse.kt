package uz.texnopos.texnoposedufinance.data.model.response

import uz.texnopos.texnoposedufinance.data.model.Report

data class MyResponse(
    var name: String = "",
    var id: String = "",
    var expenses: List<Report> = listOf(),
    var incomes: List<Report> = listOf()
)
