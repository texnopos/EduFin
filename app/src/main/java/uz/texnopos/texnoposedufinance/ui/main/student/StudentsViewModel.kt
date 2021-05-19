package uz.texnopos.texnoposedufinance.ui.main.student

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.texnopos.texnoposedufinance.core.Resource
import uz.texnopos.texnoposedufinance.data.firebase.StudentHelper
import uz.texnopos.texnoposedufinance.data.model.Student
import uz.texnopos.texnoposedufinance.data.retrofit.NetworkHelper

class StudentsViewModel(private val helper: StudentHelper, private val networkHelper: NetworkHelper): ViewModel() {
    private val _studentsList: MutableLiveData<Resource<List<Student>>> = MutableLiveData()
    val studentList: LiveData<Resource<List<Student>>>
        get() = _studentsList

    fun getAllStudents(){
        _studentsList.value = Resource.loading()
        helper.getAllStudents({
            _studentsList.value = Resource.success(it)
        },{
            _studentsList.value = Resource.error(it)
        })
    }

    private val _selectStudentsList: MutableLiveData<Resource<List<Student>>> = MutableLiveData()
    val selectStudentsList: LiveData<Resource<List<Student>>>
        get() = _selectStudentsList

    fun selectExistingStudentToGroup(groupId: String){
        _selectStudentsList.value = Resource.loading()
        networkHelper.selectExistingStudentToGroup(groupId, {
            _selectStudentsList.value = Resource.success(it)
        }, {
            _selectStudentsList.value = Resource.error(it)
        })
    }
}