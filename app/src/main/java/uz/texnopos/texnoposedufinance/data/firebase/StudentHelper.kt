package uz.texnopos.texnoposedufinance.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import uz.texnopos.texnoposedufinance.data.model.Student
import java.util.*

class StudentHelper(auth: FirebaseAuth, private val db: FirebaseFirestore) {
    private val orgId = auth.currentUser!!.uid
     fun addStudent(groupId: String, courseId: String, name: String, phone: List<String>,
                    interested: String, passport: String, birthDate: String, address: String, contract: Int,
                    onSuccess: () -> Unit, onFailure: (msg: String?) -> Unit){
         val id = UUID.randomUUID().toString()
         val newStudent = Student(id, groupId, courseId, orgId, name, phone, interested, passport, birthDate, address, contract)
         db.collection("users/$orgId/students").document(id).set(newStudent)
             .addOnSuccessListener {
                 onSuccess.invoke()
             }
             .addOnFailureListener {
                 onFailure.invoke(it.localizedMessage)
             }
     }
    fun getAllStudents(onSuccess: (List<Student>) -> Unit, onFailure: (msg: String?) -> Unit){
        db.collection("users/$orgId/students").get()
            .addOnSuccessListener { doc ->
                if(doc.documents.isNotEmpty()){
                    onSuccess.invoke(doc.documents.map {
                        it.toObject(Student::class.java) ?: Student()
                    })
                }
                else onSuccess.invoke(listOf())
            }
            .addOnFailureListener {
                onFailure.invoke(it.message)
            }
    }

    fun getSelectStudents(groupId: String, onSuccess: (List<Student>) -> Unit, onFailure: (msg: String?) -> Unit){
        db.collection("users/$orgId/students").get()
            .addOnSuccessListener { doc ->
                if(doc.documents.isNotEmpty()){
                    onSuccess.invoke(doc.documents.map {
                        it.toObject(Student::class.java) ?: Student()
                    })
                }
                else onSuccess.invoke(listOf())
            }
            .addOnFailureListener {
                onFailure.invoke(it.message)
            }
    }
}