package uz.texnopos.texnoposedufinance.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import uz.texnopos.texnoposedufinance.data.model.IncomeCategory
import uz.texnopos.texnoposedufinance.data.model.Report
import java.util.*

class ReportHelper (auth: FirebaseAuth, private val db: FirebaseFirestore) {
    val orgId = auth.currentUser!!.uid
    fun getAllIncomes(
        onAdded: (Report)->Unit,
        onModified: (Report)->Unit,
        onRemoved: (Report) -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        db.collection("users/$orgId/incomes").addSnapshotListener { value, error ->
            if (error != null) {
                onFailure.invoke(error.message.toString())
            }
            for (doc in value!!.documentChanges) {
                when(doc.type) {
                    DocumentChange.Type.ADDED -> onAdded(doc.document.toObject(Report::class.java))
                    DocumentChange.Type.MODIFIED -> onModified(doc.document.toObject(Report::class.java))
                    DocumentChange.Type.REMOVED -> onRemoved(doc.document.toObject(Report::class.java))
                }
            }
        }
    }
}