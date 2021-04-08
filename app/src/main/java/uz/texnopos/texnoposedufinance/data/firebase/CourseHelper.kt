package uz.texnopos.texnoposedufinance.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import uz.texnopos.texnoposedufinance.data.model.Course
import uz.texnopos.texnoposedufinance.data.model.Group
import java.util.*

class CourseHelper(auth: FirebaseAuth, private val db: FirebaseFirestore,
    private val func: FirebaseFunctions) {
    private val orgId = auth.currentUser!!.uid

    fun getAllCourses(
        onSuccess: (list: List<Course>) -> Unit,
        onFailure: (msg: String?) -> Unit,
    ) {
        db.collection("users/$orgId/courses").get()
            .addOnSuccessListener {doc ->
                if (doc.documents.isNotEmpty()){
                    onSuccess.invoke(doc.documents.map {
                        it.toObject(Course::class.java) ?: Course()
                    })
                }
                else onSuccess.invoke(listOf())
            }
            .addOnFailureListener {
                onFailure.invoke(it.localizedMessage)
            }
    }

    fun addNewCourse(name: String, duration: Int, price: Double,
        onSuccess: () -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        val id = UUID.randomUUID().toString()
        val newCourse = Course(
            name = name, id = id, duration = duration, price = price, orgId = orgId

        )
        db.collection("users/$orgId/courses").document(id).set(newCourse)
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

        /*db.collection("users/$orgId/groups")
            .whereEqualTo("courseId", courseId).get()
            .addOnSuccessListener {doc ->
                if(doc.documents.isNotEmpty()){
                    onSuccess.invoke(doc.documents.map {
                        it.toObject(Group::class.java) ?: Group()
                    })
                }
                else onSuccess.invoke(listOf())
            }
            .addOnFailureListener {
                onFailure.invoke(it.localizedMessage)
            }*/
    }
}