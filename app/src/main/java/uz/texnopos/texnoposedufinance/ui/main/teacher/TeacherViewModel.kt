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
        helper.getAllEmployees(
            {
                _teacherList.value = Resource.success(it)
            },
            {
                _teacherList.value = Resource.error(it)
            }
        )
    }
}