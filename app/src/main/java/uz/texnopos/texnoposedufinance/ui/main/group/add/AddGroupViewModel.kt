package uz.texnopos.texnoposedufinance.ui.main.group.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.texnopos.texnoposedufinance.core.Resource
import uz.texnopos.texnoposedufinance.data.firebase.CourseHelper
import uz.texnopos.texnoposedufinance.data.firebase.GroupHelper
import uz.texnopos.texnoposedufinance.data.firebase.TeacherHelper
import uz.texnopos.texnoposedufinance.data.model.Course
import uz.texnopos.texnoposedufinance.data.model.Group
import uz.texnopos.texnoposedufinance.data.model.Teacher

class AddGroupViewModel(private val helper: GroupHelper,
                        private val courseHelper: CourseHelper,
                        private val teacherHelper: TeacherHelper): ViewModel() {

    private val _createGroup: MutableLiveData<Resource<Group?>> = MutableLiveData()
    val createGroup: LiveData<Resource<Group?>>
        get() = _createGroup

    private val _courseList: MutableLiveData<Resource<List<Course?>>> = MutableLiveData()
    val courseList: LiveData<Resource<List<Course?>>>
        get() = _courseList

    private val _teacherList: MutableLiveData<Resource<List<Teacher>>> = MutableLiveData()
    val teacherList: LiveData<Resource<List<Teacher>>>
        get() = _teacherList

    fun createGroup(name: String,
                    teacher: String,
                    courseId: String,
                    time: String,
                    startDate: String){
        _createGroup.value = Resource.loading()
        helper.createGroup(
            name, teacher, courseId, time, startDate,
            {
                _createGroup.value = Resource.success(null)
            },
            {
                _createGroup.value = Resource.error(it)
            }
        )
    }
    fun getAllCourses(){
        _courseList.value = Resource.loading()
        courseHelper.getAllCourses(
            {
                _courseList.value = Resource.success(it)
            },
            {
                _courseList.value = Resource.error(it)
            }
        )
    }

    fun getAllTeachers() {
        _teacherList.value = Resource.loading()
        teacherHelper.getAllTeachers(
            {
                _teacherList.value = Resource.success(it)
            },
            {
                _teacherList.value = Resource.error(it)
            }
        )
    }
}