package uz.texnopos.texnoposedufinance.data.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import uz.texnopos.texnoposedufinance.data.model.Course
import uz.texnopos.texnoposedufinance.data.model.Group

interface ApiInterface {
    @GET("/getAllCourses")
    fun getAllCourses(@Query("id") userId: String): Call<List<Course>>

    @GET("/getAllStudents")
    fun getAllStudents(@Query("id") userId: String): Call<List<Group>>
}