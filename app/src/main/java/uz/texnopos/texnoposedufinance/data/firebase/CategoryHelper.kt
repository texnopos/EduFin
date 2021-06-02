package uz.texnopos.texnoposedufinance.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import uz.texnopos.texnoposedufinance.data.model.ExpenseCategory
import uz.texnopos.texnoposedufinance.data.model.IncomeCategory
import java.util.*

class CategoryHelper(auth: FirebaseAuth, private val db: FirebaseFirestore) {
    private val orgId = auth.currentUser!!.uid
    fun addIncomeCategory(name: String, onSuccess: () -> Unit,
                          onFailure: (msg: String?) -> Unit){
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
    fun getAllIncomeCategories(onSuccess: (List<IncomeCategory>) -> Unit,
                               onFailure: (msg: String?) -> Unit){
        db.collection("users/$orgId/incomeCategory").addSnapshotListener { value, error ->
            if(error != null){
                onFailure.invoke(error.message.toString())
            }
            val categories = mutableListOf<IncomeCategory>()
            for(doc in value!!){
                categories.add(doc.toObject(IncomeCategory::class.java))
                }
            onSuccess.invoke(categories)
            }
        }
        /*db.collection("users/$orgId/incomeCategory").get()
            .addOnSuccessListener {doc ->
                if(doc.documents.isNotEmpty()){
                    onSuccess.invoke(doc.documents.map {
                        it.toObject(IncomeCategory::class.java)?: IncomeCategory()
                    })
                }
                else onSuccess.invoke(listOf())
            }
            .addOnFailureListener {
                onFailure.invoke(it.message)
            }*/

    fun getAllExpenseCategories(onSuccess: (List<ExpenseCategory>) -> Unit,
                               onFailure: (msg: String?) -> Unit){

        db.collection("users/$orgId/expenseCategory").addSnapshotListener { value, error ->
            if(error != null){
                onFailure.invoke(error.message.toString())
            }
            val categories = mutableListOf<ExpenseCategory>()
            for(doc in value!!){
                categories.add(doc.toObject(ExpenseCategory::class.java))
            }
            onSuccess.invoke(categories)
        }
        /*db.collection("users/$orgId/expenseCategory").get()
            .addOnSuccessListener {doc ->
                if(doc.documents.isNotEmpty()){
                    onSuccess.invoke(doc.documents.map {
                        it.toObject(ExpenseCategory::class.java)?: ExpenseCategory()
                    })
                }
                else onSuccess.invoke(listOf())
            }
            .addOnFailureListener {
                onFailure.invoke(it.message)
            }*/
    }

    fun addExpenseCategory(name: String, onSuccess: () -> Unit,
                          onFailure: (msg: String?) -> Unit){
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
