package uz.texnopos.texnoposedufinance.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import uz.texnopos.texnoposedufinance.data.model.Organization
import uz.texnopos.texnoposedufinance.data.model.Teacher
class InfoHelper(auth: FirebaseAuth, private val db: FirebaseFirestore) {

    private val orgId = auth.currentUser!!.uid
    fun getOrgData(onSuccess: (org: Organization) -> Unit, onFailure: (msg: String?) -> Unit){
        db.collection("users").document(orgId).get()
            .addOnSuccessListener {
                onSuccess.invoke(it.toObject(Organization::class.java)!!)
            }
            .addOnFailureListener {
                onFailure.invoke(it.localizedMessage)
            }
    }

    fun updateOrgData(name: String,  address: String, score: String, mfo: String,
                                 phone: String, inn: String, director: String, bank: String,  onSuccess: () -> Unit, onFailure: (msg: String?) -> Unit){
        val org = mutableMapOf<String, Any>()
        org["name"] = name
        org["phone"] = phone
        org["address"] = address
        org["score"] = score
        org["inn"] = inn
        org["director"] = director
        org["mfo"] = mfo
        org["bank"] = bank
        db.collection("users").document(orgId).update(org)
            .addOnSuccessListener {
                onSuccess.invoke()
            }
            .addOnFailureListener {
                onFailure.invoke(it.localizedMessage)
            }
    }
}