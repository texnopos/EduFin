package uz.texnopos.texnoposedufinance.data.model

import com.google.firebase.Timestamp

data class Transaction(
    val id: String = "",
    var name: String = "",
    var amount: Int = 0,
    var createdAt: Timestamp = Timestamp.now(),
    var date: Timestamp = Timestamp.now()
)