
package uz.texnopos.texnoposedufinance.ui.main.course


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.texnopos.texnoposedufinance.core.Resource
import uz.texnopos.texnoposedufinance.data.firebase.CourseHelper
import uz.texnopos.texnoposedufinance.data.model.Course

class CoursesViewModel(private val helper: CourseHelper): ViewModel() {
    private val _courseList: MutableLiveData<Resource<List<Course>>> = MutableLiveData()
    val courseList: LiveData<Resource<List<Course>>>
        get() = _courseList

    fun getAllCourses(){
        _courseList.value = Resource.loading()
        helper.getAllCourses(
            {
                _courseList.value = Resource.success(it)
            },
            {
                _courseList.value = Resource.error(it)
            }
        )
    }

}