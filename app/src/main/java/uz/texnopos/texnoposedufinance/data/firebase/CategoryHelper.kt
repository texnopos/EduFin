package uz.texnopos.texnoposedufinance.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import uz.texnopos.texnoposedufinance.data.model.ExpenseCategory
import uz.texnopos.texnoposedufinance.data.model.IncomeCategory
import java.util.*

class CategoryHelper(auth: FirebaseAuth, private val db: FirebaseFirestore) {
    private val orgId = auth.currentUser!!.uid
    fun addIncomeCategory(
        name: String, onSuccess: () -> Unit,
        onFailure: (msg: String?) -> Unit) {
        val id = UUID.randomUUID().toString()
        val incomeCategory = IncomeCategory(name, id)
        db.collection("users/$orgId/incomeCategory").document(id).set(incomeCategory)
            .addOnSuccessListener {
                onSuccess.invoke()
            }
            .addOnFailureListener {
                onFailure.invoke(it.localizedMessage)
            }
    }

    fun getAllIncomeCategories(
        onAdded: (IncomeCategory)->Unit,
        onModified: (IncomeCategory)->Unit,
        onRemoved: (IncomeCategory) -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        db.collection("users/$orgId/incomeCategory").addSnapshotListener { value, error ->
            if (error != null) {
                onFailure.invoke(error.message.toString())
            }
            for (doc in value!!.documentChanges) {
                when(doc.type) {
                    DocumentChange.Type.ADDED -> onAdded(doc.document.toObject(IncomeCategory::class.java))
                    DocumentChange.Type.MODIFIED -> onModified(doc.document.toObject(IncomeCategory::class.java))
                    DocumentChange.Type.REMOVED -> onRemoved(doc.document.toObject(IncomeCategory::class.java))
                }
            }
        }
    }

    fun getAllExpenseCategories(
        onAdded: (ExpenseCategory)->Unit,
        onModified: (ExpenseCategory)->Unit,
        onRemoved: (ExpenseCategory) -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        db.collection("users/$orgId/expenseCategory").addSnapshotListener { value, error ->
            if (error != null) {
                onFailure.invoke(error.message.toString())
            }
            for (doc in value!!.documentChanges) {
                when(doc.type) {
                    DocumentChange.Type.ADDED -> onAdded(doc.document.toObject(ExpenseCategory::class.java))
                    DocumentChange.Type.MODIFIED -> onModified(doc.document.toObject(ExpenseCategory::class.java))
                    DocumentChange.Type.REMOVED -> onRemoved(doc.document.toObject(ExpenseCategory::class.java))
                }
            }
        }
        /*db.collection("users/$orgId/expenseCategory").addSnapshotListener { value, error ->
            if (error != null) {
                onFailure.invoke(error.message.toString())
            }
            val categories = mutableListOf<ExpenseCategory>()
            for (doc in value!!) {
                categories.add(doc.toObject(ExpenseCategory::class.java))
            }
            onSuccess.invoke(categories)
        }*/
    }

    fun addExpenseCategory(
        name: String, onSuccess: () -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        val id = UUID.randomUUID().toString()
        val expenseCategory = ExpenseCategory(name, id)
        db.collection("users/$orgId/expenseCategory").document(id).set(expenseCategory)
            .addOnSuccessListener {
                onSuccess.invoke()
            }
            .addOnFailureListener {
                onFailure.invoke(it.localizedMessage)
            }
    }
}
