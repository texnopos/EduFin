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

class AddGroupViewModel(private val helper: GroupHelper): ViewModel() {

    private val _createGroup: MutableLiveData<Resource<Group?>> = MutableLiveData()
    val createGroup: LiveData<Resource<Group?>>
        get() = _createGroup

    fun createGroup(name: String,
                    teacher: String,
                    courseId: String,
                    courseName: String,
                    time: String,
                    startDate: String,
                    days: String,
                    created: String){
        _createGroup.value = Resource.loading()
        helper.createGroup(
            name, teacher, courseId, courseName, time, startDate, days, created,
            {
                _createGroup.value = Resource.success(Group())
            },
            {
                _createGroup.value = Resource.error(it)
            }
        )
    }
}