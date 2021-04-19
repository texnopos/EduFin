package uz.texnopos.texnoposedufinance.data.retrofit

import retrofit2.Call
import retrofit2.http.GET
import uz.texnopos.texnoposedufinance.data.model.Course

interface ApiInterface {
    @GET("/getAllCourses")
    fun getAllCourses(): Call<List<Course>>
}