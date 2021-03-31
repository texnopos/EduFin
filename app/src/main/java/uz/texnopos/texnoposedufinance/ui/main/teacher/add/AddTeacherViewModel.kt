package uz.texnopos.texnoposedufinance.ui.main.teacher.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.texnopos.texnoposedufinance.core.Resource
import uz.texnopos.texnoposedufinance.data.firebase.TeacherHelper
import uz.texnopos.texnoposedufinance.data.model.Teacher

class AddTeacherViewModel(private val helper: TeacherHelper) : ViewModel() {

    private val _createTeacher: MutableLiveData<Resource<Teacher>> = MutableLiveData()
    val createTeacher: LiveData<Resource<Teacher>>
        get() = _createTeacher

    fun createTeacher(name: String, phone: String, username: String, password: String, salary: Double) {
        _createTeacher.value = Resource.loading()
        helper.createTeacher(
            name, phone, username, password, salary,
            {
                _createTeacher.value = Resource.success(null)
            },
            {
                _createTeacher.value = Resource.error(it)
            }
        )
    }
}