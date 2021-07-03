package uz.texnopos.texnoposedufinance.data

import uz.texnopos.texnoposedufinance.data.model.Report

data class AllReports(var category: String = "",
                      var amount: Int = 0,
                      var trans: Int = 0,
                      var expenses: List<Report> = listOf(),
                      var incomes: List<Report> = listOf())