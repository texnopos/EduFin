package uz.texnopos.texnoposedufinance.ui.main.student.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.texnopos.texnoposedufinance.core.Resource
import uz.texnopos.texnoposedufinance.data.firebase.StudentHelper
import uz.texnopos.texnoposedufinance.data.model.Student

class AddStudentViewModel(private val helper: StudentHelper): ViewModel() {
    private val _student: MutableLiveData<Resource<Student>> = MutableLiveData()
    val student: LiveData<Resource<Student>>
        get() = _student

    fun addStudent(groupId: String, name: String, phone: String){
        _student.value = Resource.loading()
        helper.addStudent(groupId, name, phone,{
                _student.value = Resource.success(Student())
            }, {
                _student.value = Resource.error(it)
            })
    }
}