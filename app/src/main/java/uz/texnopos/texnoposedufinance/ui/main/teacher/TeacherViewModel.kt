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


    fun getAllEmployees() {
        helper.getAllTeachers(
            {
                _teacherList.value = Resource.success(it)
            },
            {
                _teacherList.value = Resource.error(it)
            }
        )
    }
    private val _deleted: MutableLiveData<Resource<Teacher>> = MutableLiveData()
    val deleted: LiveData<Resource<Teacher>>
        get() = _deleted

    private val _current: MutableLiveData<Resource<Teacher>> = MutableLiveData()
    val current: LiveData<Resource<Teacher>>
        get() = _current

    fun deleteTeacher(teacherId: String){
        helper.deleteTeacher(teacherId,
            {
                _deleted.value = Resource.success(Teacher())
            },
            {
                _deleted.value = Resource.error(it)
            })
    }
    fun getDataCurrentTeacher(teacherId: String){
        helper.getDataCurrentTeacher(teacherId,
            {
                _current.value = Resource.success(Teacher())
            },
            {
                _current.value = Resource.error(it)
            })
    }
}