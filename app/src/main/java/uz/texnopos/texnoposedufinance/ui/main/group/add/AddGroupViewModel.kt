package uz.texnopos.texnoposedufinance.ui.main.group.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.texnopos.texnoposedufinance.core.Resource
import uz.texnopos.texnoposedufinance.data.firebase.CourseHelper
import uz.texnopos.texnoposedufinance.data.firebase.GroupHelper
import uz.texnopos.texnoposedufinance.data.model.Course
import uz.texnopos.texnoposedufinance.data.model.Group

class AddGroupViewModel(private val helper: GroupHelper, private val courseHelper: CourseHelper): ViewModel() {

    private val _createGroup: MutableLiveData<Resource<Group?>> = MutableLiveData()
    val createGroup: LiveData<Resource<Group?>>
        get() = _createGroup

    private val _courseList: MutableLiveData<Resource<List<Course?>>> = MutableLiveData()
    val courseList: LiveData<Resource<List<Course?>>>
        get() = _courseList

    fun createGroup(name: String, groupNum: String, courseId: String){
        _createGroup.value = Resource.loading()
        helper.createGroup(
            name, groupNum, courseId,
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
}