package uz.texnopos.texnoposedufinance.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import uz.texnopos.texnoposedufinance.data.model.Group
import uz.texnopos.texnoposedufinance.data.model.Teacher
import java.util.*

class GroupHelper(auth: FirebaseAuth, private val db: FirebaseFirestore) {
    private val orgId = auth.currentUser!!.uid

    fun createGroup(name: String,
                    teacher: String,
                    courseId: String,
                    courseName: String,
                    time: String,
                    startDate: String,
                    days: String,
                    created: String,
                    onSuccess: () -> Unit,
                    onFailure: (msg: String?) -> Unit
    ){
        val id = UUID.randomUUID().toString()
        val newGroup = Group(
            id = id,
            courseId = courseId,
            courseName = courseName,
            name = name,
            time = time,
            startDate = startDate,
            teacher = teacher,
            days = days,
            created = created
        )
        db.collection("users/$orgId/groups").document(id).set(newGroup)
            .addOnSuccessListener {
                onSuccess.invoke()
            }
            .addOnFailureListener {
                onFailure.invoke(it.localizedMessage)
            }
    }

    fun deleteGroup(groupId: String, onSuccess: () -> Unit, onFailure: (msg: String?) -> Unit){
        db.collection("users/$orgId/groups").document(groupId).delete()
            .addOnSuccessListener {
                onSuccess.invoke()
            }
            .addOnFailureListener {
                onFailure.invoke(it.localizedMessage)
            }
    }
    fun getDataCurrentGroup(groupId: String, onSuccess: (group: Group) -> Unit, onFailure: (msg: String?) -> Unit){
        db.collection("users/$orgId/groups").document(groupId).get()
            .addOnSuccessListener {
                onSuccess.invoke(it.toObject(Group::class.java)!!)
            }
            .addOnFailureListener {
                onFailure.invoke(it.localizedMessage)
            }
    }
}