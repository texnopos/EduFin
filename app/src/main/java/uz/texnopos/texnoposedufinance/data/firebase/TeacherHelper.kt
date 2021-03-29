package uz.texnopos.texnoposedufinance.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import uz.texnopos.texnoposedufinance.data.model.Teacher
import java.util.*

class TeacherHelper(private val db: FirebaseFirestore, private val auth: FirebaseAuth) {
    private val orgId = auth.currentUser!!.uid
    fun getAllEmployees(
        onSuccess: (list: List<Teacher>) -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        db.collection("users/$orgId/teachers").get()
            .addOnSuccessListener { collection ->
                if (collection.documents.isNotEmpty()) {
                    onSuccess.invoke(collection.documents.map {
                        it.toObject(Teacher::class.java) ?: Teacher()
                    })
                } else {
                    onSuccess.invoke(listOf())
                }
            }
            .addOnFailureListener {
                onFailure.invoke(it.localizedMessage)
            }
    }

    fun createTeacher(
        name: String, phone: String, username: String, password: String,
        onSuccess: () -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        val id = UUID.randomUUID().toString()
        val newTeacher = Teacher(
            name = name, phone = phone, id = id, username = username, password = password, orgId = orgId
        )
        db.collection("users/$orgId/teachers").document(id).set(newTeacher)
            .addOnSuccessListener {
                onSuccess.invoke()
            }
            .addOnFailureListener {
                onFailure.invoke(it.localizedMessage)
            }
    }

}