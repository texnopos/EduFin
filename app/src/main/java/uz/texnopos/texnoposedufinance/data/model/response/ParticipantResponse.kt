package uz.texnopos.texnoposedufinance.data.model.response

import uz.texnopos.texnoposedufinance.data.model.CoursePayments
import uz.texnopos.texnoposedufinance.data.model.Participant
import uz.texnopos.texnoposedufinance.data.model.Student

data class ParticipantResponse (
    var participant: Participant,
    var student: Student,
    var payments: List<CoursePayments>,
    var position: Int,
    var paid: Boolean
)