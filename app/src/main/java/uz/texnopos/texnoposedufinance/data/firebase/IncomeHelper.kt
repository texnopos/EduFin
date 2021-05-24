package uz.texnopos.texnoposedufinance.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import uz.texnopos.texnoposedufinance.data.model.CoursePayments
import java.util.*

class IncomeHelper(auth: FirebaseAuth, private val db: FirebaseFirestore) {
    val orgId = auth.currentUser!!.uid
    fun addCoursePaymentToIncome(amount: Int, date: Long, createdDate: Long, participantId: String,
                                 groupId: String, courseId: String, onSuccess: () -> Unit, onFailure: (msg: String?) -> Unit){
        val id = UUID.randomUUID().toString()
        val newPayment = CoursePayments(id, amount, date, createdDate, participantId, groupId, courseId)
        db.collection("users/$orgId/incomes").document(id).set(newPayment)
            .addOnSuccessListener { onSuccess.invoke() }
            .addOnFailureListener { onFailure.invoke(it.message) }
    }
}