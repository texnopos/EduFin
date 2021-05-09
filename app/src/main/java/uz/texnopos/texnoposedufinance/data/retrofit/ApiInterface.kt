package uz.texnopos.texnoposedufinance.data.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import uz.texnopos.texnoposedufinance.data.model.Course
import uz.texnopos.texnoposedufinance.data.model.ParticipantResponse

interface ApiInterface {
    @GET("/getAllCourses")
    fun getAllCourses(@Query("id") userId: String): Call<List<Course>>

    @GET("/getGroupParticipants")
    fun getGroupParticipants(@Query("orgId") orgId: String, @Query("groupId") id: String): Call<List<ParticipantResponse>>
}