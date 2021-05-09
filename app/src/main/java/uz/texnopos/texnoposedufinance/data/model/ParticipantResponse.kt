package uz.texnopos.texnoposedufinance.data.model

data class ParticipantResponse (
    val student: Student,
    val payments: List<Payment>
)