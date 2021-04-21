package uz.texnopos.texnoposedufinance.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import uz.texnopos.texnoposedufinance.data.model.Course
import uz.texnopos.texnoposedufinance.data.model.Group
import java.util.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.texnopos.texnoposedufinance.data.retrofit.ApiInterface


class CourseHelper(
    auth: FirebaseAuth, private val db: FirebaseFirestore,
    private val functions: FirebaseFunctions
) {
    private val orgId = auth.currentUser!!.uid

    fun addNewCourse(
        name: String, duration: Int, price: Double,
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
}