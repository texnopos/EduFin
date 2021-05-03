package uz.texnopos.texnoposedufinance.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import uz.texnopos.texnoposedufinance.data.model.Student
import java.util.*

class StudentHelper(auth: FirebaseAuth, private val db: FirebaseFirestore) {
    private val orgId = auth.currentUser!!.uid
     fun addStudent(groupId: String, name: String, phone: String, onSuccess: () -> Unit,
                    onFailure: (msg: String?) -> Unit){
         val id = UUID.randomUUID().toString()
         val newStudent = Student(id, orgId, name, phone, groupId)
         db.collection("users/$orgId/students").document(id).set(newStudent)
             .addOnSuccessListener {
                 onSuccess.invoke()
             }
             .addOnFailureListener {
                 onFailure.invoke(it.localizedMessage)
             }
     }
}