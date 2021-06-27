package uz.texnopos.texnoposedufinance.ui.main.course


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.texnopos.texnoposedufinance.core.Resource
import uz.texnopos.texnoposedufinance.data.firebase.CourseHelper
import uz.texnopos.texnoposedufinance.data.model.Course
import uz.texnopos.texnoposedufinance.data.retrofit.NetworkHelper

class CourseViewModel(private val helper: CourseHelper) : ViewModel() {
    private val _createCourse: MutableLiveData<Resource<Course>> = MutableLiveData()
    val createCourse: LiveData<Resource<Course>>
        get() = _createCourse

    fun createCourse(name: String, duration: Int, price: Int) {
        _createCourse.value = Resource.loading()
        helper.addNewCourse(name, duration, price, {
            _createCourse.value = Resource.success(Course())
        }, {
            _createCourse.value = Resource.error(it)
        }
        )
    }
}