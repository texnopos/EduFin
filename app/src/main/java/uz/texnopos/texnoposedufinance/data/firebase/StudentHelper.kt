package uz.texnopos.texnoposedufinance.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import uz.texnopos.texnoposedufinance.data.model.Participant
import uz.texnopos.texnoposedufinance.data.model.Student
import java.util.*

class StudentHelper(auth: FirebaseAuth, private val db: FirebaseFirestore) {
    private val orgId = auth.currentUser!!.uid
     fun addStudent(id: String, name: String, phone: List<String>, interested: String, passport: String,
                    birthDate: Long, address: String, onSuccess: () -> Unit, onFailure: (msg: String?) -> Unit){
         val newStudent = Student(id, name, phone, interested, passport, birthDate, address)
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

    fun getStudentByPassport(passport: String, onSuccess: (List<Student>) -> Unit, onFailure: (msg: String?) -> Unit){
        db.collection("users/$orgId/students").whereEqualTo("passport", passport).get()
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
    /*fun createParticipant(id: String, studentId: String, groupId: String, courseId: String, contract: Int, onSuccess: () -> Unit, onFailure: (msg: String?) -> Unit){
        val participant = Participant(id, studentId, groupId, courseId, contract)
        db.collection("users/$orgId/participants").document(id).set(participant)
            .addOnFailureListener {
                onFailure.invoke(it.message)
            }
            .addOnSuccessListener { onSuccess.invoke() }
    }*/
}