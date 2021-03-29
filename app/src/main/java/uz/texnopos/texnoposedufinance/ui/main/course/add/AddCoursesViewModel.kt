package uz.texnopos.texnoposedufinance.ui.main.course.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.texnopos.texnoposedufinance.core.Resource
import uz.texnopos.texnoposedufinance.data.firebase.CourseHelper
import uz.texnopos.texnoposedufinance.data.firebase.TeacherHelper
import uz.texnopos.texnoposedufinance.data.model.Course
import uz.texnopos.texnoposedufinance.data.model.Teacher

class AddCoursesViewModel(
    private val helper: CourseHelper,
    private val teacherHelper: TeacherHelper
) : ViewModel() {

    private val _createCourse: MutableLiveData<Resource<Course>> = MutableLiveData()
    private val _teacherList: MutableLiveData<Resource<List<Teacher>>> = MutableLiveData()


    val createCourse: LiveData<Resource<Course>>
        get() = _createCourse

    val teacherList: LiveData<Resource<List<Teacher>>>
        get() = _teacherList

    fun createCourse(name: String, teacher: String, cost: Double, period: Int) {
        _createCourse.value = Resource.loading()
        helper.addNewCourse(
            name, teacher, cost, period,
            {
                _createCourse.value = Resource.success(null)
            },
            {
                _createCourse.value = Resource.error(it)
            }
        )
    }

    fun getAllTeachers() {
        _teacherList.value = Resource.loading()
        teacherHelper.getAllEmployees(
            {
                _teacherList.value = Resource.success(it)
            },
            {
                _teacherList.value = Resource.error(it)
            }
        )
    }
}