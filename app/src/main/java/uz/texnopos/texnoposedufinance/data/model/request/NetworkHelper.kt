package uz.texnopos.texnoposedufinance.data.model.request

import retrofit2.*
import uz.texnopos.texnoposedufinance.data.model.Course
import uz.texnopos.texnoposedufinance.data.retrofit.ApiInterface
import uz.texnopos.texnoposedufinance.ui.main.course.CoursesAdapter

class NetworkHelper(private val apiInterface: ApiInterface) {
//    fun postId(
//        id: String,
//        onSuccess: (CourseRequest) -> Unit,
//        onFailure: (msg: String) -> Unit
//    ) {
//        val call: Call<CourseRequest> = apiInterface.postId(id)
//        call.enqueue(object : Callback<CourseRequest> {
//            override fun onFailure(call: Call<CourseRequest>, t: Throwable) {
//                onFailure.invoke(t.localizedMessage!!)
//            }
//
//            override fun onResponse(call: Call<CourseRequest>, response: Response<CourseRequest>) {
//                response.body().let {
//                    onSuccess.invoke(it!!)
//                }
//            }
//        })
//    }

    fun getAllCourses(
        orgId: String,
        onSuccess: (List<Course>) -> Unit,
        onFailure: (msg: String) -> Unit
    ) {
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
}