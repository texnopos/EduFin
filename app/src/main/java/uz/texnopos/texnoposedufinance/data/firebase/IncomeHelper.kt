package uz.texnopos.texnoposedufinance.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import uz.texnopos.texnoposedufinance.data.model.Payment
import java.util.*

class IncomeHelper(auth: FirebaseAuth, private val db: FirebaseFirestore) {
    val orgId = auth.currentUser!!.uid
    fun addCoursePayment(amount: Int, date: String, createdDate: String, participantId: String,
                         groupId: String, courseId: String, onSuccess: () -> Unit, onFailure: (msg: String?) -> Unit){
        val id = UUID.randomUUID().toString()
        val newPayment = Payment(id, amount, date, createdDate, participantId, groupId, courseId)
        db.collection("users/$orgId/incomes").document(id).set(newPayment)
            .addOnSuccessListener { onSuccess.invoke() }
            .addOnFailureListener { onFailure.invoke(it.message) }
    }
}