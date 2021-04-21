package uz.texnopos.texnoposedufinance.data.model.request

import retrofit2.*
import uz.texnopos.texnoposedufinance.data.model.Course
import uz.texnopos.texnoposedufinance.data.retrofit.ApiInterface
import uz.texnopos.texnoposedufinance.ui.main.course.CoursesAdapter

class NetworkHelper(private val apiInterface: ApiInterface) {
    fun getAllCourses(
        onSuccess: (List<Course>) -> Unit,
        onFailure: (msg: String) -> Unit
    ) {

        val call: Call<List<Course>> = apiInterface.getAllCourses()
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
}