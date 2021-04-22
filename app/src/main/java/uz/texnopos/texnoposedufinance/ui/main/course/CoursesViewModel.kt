package uz.texnopos.texnoposedufinance.ui.main.course


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import uz.texnopos.texnoposedufinance.core.Resource
import uz.texnopos.texnoposedufinance.data.firebase.AuthHelper
import uz.texnopos.texnoposedufinance.data.firebase.CourseHelper
import uz.texnopos.texnoposedufinance.data.model.Course
import uz.texnopos.texnoposedufinance.data.model.Group
import uz.texnopos.texnoposedufinance.data.model.request.CourseRequest
import uz.texnopos.texnoposedufinance.data.model.request.NetworkHelper

class CoursesViewModel(private val networkHelper: NetworkHelper,) : ViewModel() {
    private val _courseList: MutableLiveData<Resource<List<Course>>> = MutableLiveData()
    val courseList: LiveData<Resource<List<Course>>>
        get() = _courseList

    fun getAllCourses(orgId: String) {
        _courseList.value = Resource.loading()
        networkHelper.getAllCourses(
            orgId,
            {
                _courseList.value = Resource.success(it)
            },
            {
                _courseList.value = Resource.error(it)
            }
        )
    }
}