package uz.texnopos.texnoposedufinance.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import uz.texnopos.texnoposedufinance.data.model.Course
import uz.texnopos.texnoposedufinance.data.model.Teacher
import java.util.*

class TeacherHelper(auth: FirebaseAuth, private val db: FirebaseFirestore) {
    private val orgId = auth.currentUser!!.uid
    fun getAllTeachers(
        onSuccess: (list: List<Teacher>) -> Unit,
        onFailure: (msg: String?) -> Unit) {
        db.collection("users/$orgId/teachers").get()
            .addOnSuccessListener {doc ->
                if(doc.documents.isNotEmpty()){
                    onSuccess.invoke(doc.documents.map {
                        it.toObject(Teacher::class.java) ?: Teacher()
                    })
                }
                else onSuccess.invoke(listOf())
            }
            .addOnFailureListener {
                onFailure.invoke(it.localizedMessage)
            }
    }

    fun createTeacher(
        name: String, phone: String, username: String, password: String, salary: String,
        onSuccess: () -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        val id = UUID.randomUUID().toString()
        val newTeacher = Teacher(
            name = name,
            phone = phone,
            id = id,
            username = username,
            password = password,
            salary = salary
        )
        db.collection("users/$orgId/teachers").document(id).set(newTeacher)
            .addOnSuccessListener {
                onSuccess.invoke()
            }
            .addOnFailureListener {
                onFailure.invoke(it.localizedMessage)
            }
    }
    fun deleteTeacher(teacherId: String, onSuccess: () -> Unit, onFailure: (msg: String?) -> Unit){
        db.collection("users/$orgId/teachers").document(teacherId).delete()
            .addOnSuccessListener {
                onSuccess.invoke()
            }
            .addOnFailureListener {
                onFailure.invoke(it.localizedMessage)
            }
    }
    fun getDataCurrentTeacher(teacherId: String, onSuccess: (teacher: Teacher) -> Unit, onFailure: (msg: String?) -> Unit){
        db.collection("users/$orgId/teachers").document(teacherId).get()
            .addOnSuccessListener {
                onSuccess.invoke(it.toObject(Teacher::class.java)!!)
            }
            .addOnFailureListener {
                onFailure.invoke(it.localizedMessage)
            }
    }
    fun updateDataCurrentTeacher(teacherId: String,  name: String, phone: String, username: String,
                                 salary: String, onSuccess: () -> Unit, onFailure: (msg: String?) -> Unit){
        val teacher = mutableMapOf<String, Any>()
        teacher["name"] = name
        teacher["phone"] = phone
        teacher["username"] = username
        teacher["salary"] = salary
        db.collection("users/$orgId/teachers").document(teacherId).update(teacher)
            .addOnSuccessListener {
                onSuccess.invoke()
            }
            .addOnFailureListener {
                onFailure.invoke(it.localizedMessage)
            }
    }

}

