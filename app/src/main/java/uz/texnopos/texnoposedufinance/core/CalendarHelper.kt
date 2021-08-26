package uz.texnopos.texnoposedufinance.core

import java.text.SimpleDateFormat
import java.util.*

class CalendarHelper {
    val currentDateInMillis: Long
    get() = System.currentTimeMillis()

    val currentDate: String
    get() {
        val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.ROOT)
        return sdf.format(currentDateInMillis)
    }

    val beginningOfMonth: String
    get() {
        val sdf = SimpleDateFormat("MM.yyyy", Locale.ROOT)
        return "01.${sdf.format(currentDateInMillis)}"
    }

    val beginningOfMothInMillis: Long
    get() {
        val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.ROOT)
        return sdf.parse(beginningOfMonth)!!.time
    }
}