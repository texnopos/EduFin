package uz.texnopos.texnoposedufinance.ui.main.teacher

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.texnopos.texnoposedufinance.core.Resource
import uz.texnopos.texnoposedufinance.data.firebase.TeacherHelper
import uz.texnopos.texnoposedufinance.data.model.Teacher

class TeacherViewModel(private val helper: TeacherHelper) : ViewModel() {
    private val _teacherList: MutableLiveData<Resource<List<Teacher>>> = MutableLiveData()
    val teacherList: LiveData<Resource<List<Teacher>>>
        get() = _teacherList

    fun getAllTeachers() {
        _teacherList.value = Resource.loading()
        helper.getAllTeachers({
                _teacherList.value = Resource.success(it)
            }, {
                _teacherList.value = Resource.error(it)
            }
        )
    }

    private val _deleted: MutableLiveData<Resource<Teacher>> = MutableLiveData()
    val deleted: LiveData<Resource<Teacher>>
        get() = _deleted

    fun deleteTeacher(teacherId: String) {
        _deleted.value = Resource.loading()
        helper.deleteTeacher(teacherId, {
                _deleted.value = Resource.success(Teacher())
            }, {
                _deleted.value = Resource.error(it)
            })
    }

    private val _current: MutableLiveData<Resource<Teacher>> = MutableLiveData()
    val current: LiveData<Resource<Teacher>>
        get() = _current
    fun getDataCurrentTeacher(teacherId: String) {
        _current.value = Resource.loading()
        helper.getDataCurrentTeacher(teacherId, {
                _current.value = Resource.success(it)
            }, {
                _current.value = Resource.error(it)
            })
    }

    private val _updateTeacher: MutableLiveData<Resource<Teacher>> = MutableLiveData()
    val updateTeacher: LiveData<Resource<Teacher>>
        get() = _updateTeacher
    fun updateDataCurrentTeacher(teacherId: String, name: String, phone: String, username: String, salary: String){
        _updateTeacher.value = Resource.loading()
        helper.updateDataCurrentTeacher(teacherId, name, phone, username, salary, {
                _updateTeacher.value = Resource.success(Teacher())
            }, {
                _updateTeacher.value = Resource.error(it)
            })
    }
}