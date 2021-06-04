package uz.texnopos.texnoposedufinance.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import uz.texnopos.texnoposedufinance.data.model.CoursePayments
import uz.texnopos.texnoposedufinance.data.model.Income
import java.util.*

class ReportHelper (auth: FirebaseAuth, private val db: FirebaseFirestore) {
    val orgId = auth.currentUser!!.uid
    fun addIncome(amount: Int, date: Long, createdDate: Long, category: String, onSuccess: () -> Unit, onFailure: (msg: String?) -> Unit){
        val id = UUID.randomUUID().toString()
        val newIncome = Income(id = id, amount = amount, date = date, createdDate = createdDate, category = category)
        db.collection("users/$orgId/incomes").document(id).set(newIncome)
            .addOnSuccessListener { onSuccess.invoke() }
            .addOnFailureListener { onFailure.invoke(it.message) }
    }
    fun addExpense(amount: Int, date: Long, createdDate: Long, category: String, onSuccess: () -> Unit, onFailure: (msg: String?) -> Unit){
        val id = UUID.randomUUID().toString()
        val newExpense = Income(id = id, amount = amount, date = date, createdDate = createdDate, category = category)
        db.collection("users/$orgId/expenses").document(id).set(newExpense)
            .addOnSuccessListener { onSuccess.invoke() }
            .addOnFailureListener { onFailure.invoke(it.message) }
    }
}