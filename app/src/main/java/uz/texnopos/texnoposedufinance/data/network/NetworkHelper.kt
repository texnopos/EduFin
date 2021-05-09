package uz.texnopos.texnoposedufinance.data.network

import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.texnopos.texnoposedufinance.data.model.Course
import uz.texnopos.texnoposedufinance.data.model.ParticipantResponse
import uz.texnopos.texnoposedufinance.data.retrofit.ApiInterface

class NetworkHelper(auth: FirebaseAuth, private val apiInterface: ApiInterface) {
    private val orgId = auth.currentUser!!.uid
    fun getAllCourses(onSuccess: (List<Course>) -> Unit, onFailure: (msg: String) -> Unit) {
        val call: Call<List<Course>> = apiInterface.getAllCourses(orgId)
        call.enqueue(object : Callback<List<Course>> {
            override fun onFailure(call: Call<List<Course>>, t: Throwable) {
                onFailure.invoke(t.localizedMessage!!)
            }
            override fun onResponse(call: Call<List<Course>>, response: Response<List<Course>>) {
                response.body().let {
                    onSuccess.invoke(it!!)
                }
            }

        })
    }

    fun getGroupParticipants(id: String,
                             onSuccess: (List<ParticipantResponse>) -> Unit,
                             onFailure: (msg: String) -> Unit) {
        val call: Call<List<ParticipantResponse>> = apiInterface.getGroupParticipants(orgId, id)
        call.enqueue(object: Callback<List<ParticipantResponse>>{
            override fun onFailure(call: Call<List<ParticipantResponse>>, t: Throwable) {
                onFailure.invoke(t.localizedMessage!!)
            }
            override fun onResponse(
                call: Call<List<ParticipantResponse>>,
                response: Response<List<ParticipantResponse>>) {
                response.body().let{
                    onSuccess.invoke(it!!)
                }
            }
        })

    }
}