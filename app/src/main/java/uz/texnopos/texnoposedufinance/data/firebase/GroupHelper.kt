package uz.texnopos.texnoposedufinance.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import uz.texnopos.texnoposedufinance.data.model.Group
import java.util.*

class GroupHelper(private val auth: FirebaseAuth, private val db: FirebaseFirestore) {
    fun createGroup(courseName: String, groupNum: String, courseId: String,
                    onSuccess: () -> Unit,
                    onFailure: (msg: String?) -> Unit
    ){
        val id = UUID.randomUUID().toString()
        val newGroup = Group(
            courseName = courseName, id = id, groupNum = groupNum, courseId = courseId, name = courseName + groupNum
        )
        db.collection("users/${auth.currentUser!!.uid}/courses/$courseId/groups").document(id).set(newGroup)
            .addOnSuccessListener {
                onSuccess.invoke()
            }
            .addOnFailureListener {
                onFailure.invoke(it.localizedMessage)
            }
    }
    fun getAllGroups(
        courseId: String,
        onSuccess: (list: List<Group>) -> Unit,
        onFailure: (msg: String?) -> Unit
    ){
        db.collection("users/${auth.currentUser!!.uid}/courses/$courseId/groups").get()
            .addOnSuccessListener {doc ->
                if (doc.documents.isNotEmpty()){
                    val gList: MutableList<Group> = mutableListOf()
                    doc.documents.forEach{gr->
                        val group = gr.toObject(Group::class.java)
                        group!!.id = gr["id"].toString()
                        group.courseName = gr["courseName"].toString()
                        group.groupNum = gr["groupNum"].toString()
                        group.courseId = gr["courseId"].toString()
                        group.name = gr["name"].toString()
                        group.let { gList.add(it) }
                    }
                    onSuccess.invoke(gList)
                }
                else onSuccess.invoke(listOf())
            }
            .addOnFailureListener {
                onFailure.invoke(it.localizedMessage)
            }
    }
}