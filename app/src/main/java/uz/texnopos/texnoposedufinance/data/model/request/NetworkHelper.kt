package uz.texnopos.texnoposedufinance.data.model.request

import retrofit2.*
import uz.texnopos.texnoposedufinance.data.model.Course
import uz.texnopos.texnoposedufinance.data.retrofit.ApiInterface
import uz.texnopos.texnoposedufinance.ui.main.course.CoursesAdapter

class NetworkHelper(private val api: Retrofit){
    fun getAllCourses(adapter: CoursesAdapter){
        val call: Call<List<Course>> = api.create(ApiInterface::class.java).getAllCourses()
        call.enqueue(object: Callback<List<Course>>{
            override fun onFailure(call: Call<List<Course>>, t: Throwable) {
                adapter.onFailure.invoke(t.localizedMessage)
            }

            override fun onResponse(call: Call<List<Course>>, response: Response<List<Course>>) {
                response.body().let {
                    adapter.onResponse.invoke(it!!)
                }
            }

        })
    }
}