package uz.texnopos.texnoposedufinance.ui.main.course.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.texnopos.texnoposedufinance.core.Resource
import uz.texnopos.texnoposedufinance.data.firebase.CourseHelper
import uz.texnopos.texnoposedufinance.data.firebase.TeacherHelper
import uz.texnopos.texnoposedufinance.data.model.Course
import uz.texnopos.texnoposedufinance.data.model.Teacher

class AddCourseViewModel(private val helper: CourseHelper): ViewModel() {

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