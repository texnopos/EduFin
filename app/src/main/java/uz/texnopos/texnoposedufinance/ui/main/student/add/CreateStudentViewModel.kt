package uz.texnopos.texnoposedufinance.ui.main.student.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.texnopos.texnoposedufinance.core.Resource
import uz.texnopos.texnoposedufinance.data.firebase.StudentHelper
import uz.texnopos.texnoposedufinance.data.model.*
import uz.texnopos.texnoposedufinance.data.model.response.PostResponse
import uz.texnopos.texnoposedufinance.data.retrofit.NetworkHelper

class CreateStudentViewModel(
    private val helper: StudentHelper,
    private val networkHelper: NetworkHelper
) : ViewModel() {
    private val _createStudent: MutableLiveData<Resource<Student>> = MutableLiveData()
    val createStudent: LiveData<Resource<Student>>
        get() = _createStudent

    fun addStudent(
        id: String, name: String, phone: List<String>, interested: String, passport: String,
        birthDate: Long, createdDate: Long, address: String
    ) {
        _createStudent.value = Resource.loading()
        helper.addStudent(id, name, phone, interested, passport, birthDate, createdDate, address, {
            _createStudent.value = Resource.success(Student())
        }, {
            _createStudent.value = Resource.error(it)
        })
    }

    private val _student: MutableLiveData<Resource<String>> = MutableLiveData()
    val student: LiveData<Resource<String>>
        get() = _student

    fun createParticipantWithNewStudent(data: CreateParticipantRequest) {
        _student.value = Resource.loading()
        networkHelper.createParticipantWithNewStudent(data, {
            _student.value = Resource.success(it)
        }, {
            _student.value = Resource.error(it)
        }
        )
    }

    private val _createParticipant: MutableLiveData<Resource<String>> = MutableLiveData()
    val createParticipant: LiveData<Resource<String>>
        get() = _createParticipant

    fun createParticipantIfStudentNotExists(data: CreateParticipantRequest) {
        _createParticipant.value = Resource.loading()
        networkHelper.createParticipantIfStudentNotExists(data, {
            _createParticipant.value = Resource.success(it)
        }, {
            _createParticipant.value = Resource.error(it)
        }
        )
    }

    private val _createParticipantWithStudentId: MutableLiveData<Resource<String>> =
        MutableLiveData()
    val createParticipantWithStudentId: LiveData<Resource<String>>
        get() = _createParticipantWithStudentId

    fun createParticipantWithStudentId(data: SendParticipantDataRequest) {
        _createParticipantWithStudentId.value = Resource.loading()
        networkHelper.createParticipantWithStudentId(data, {
            _createParticipantWithStudentId.value = Resource.success(it)
        }, {
            _createParticipantWithStudentId.value = Resource.error(it)
        })
    }

    private val _studentList: MutableLiveData<Resource<List<Student>>> =
        MutableLiveData()
    val studentsList: LiveData<Resource<List<Student>>>
        get() = _studentList

    fun getStudentByPassport(passport: String) {
        _studentList.value = Resource.loading()
        helper.getStudentByPassport(passport, {
            _studentList.value = Resource.success(it)
        }, {
            _studentList.value = Resource.error(it)
        })
    }

    private val _contract: MutableLiveData<Resource<String>> = MutableLiveData()
    val contract: LiveData<Resource<String>>
        get() = _contract

    fun checkContract(data: ContractRequest) {
        _contract.value = Resource.loading()
        networkHelper.checkContract(data, {
            _contract.value = Resource.success(it)
        }, {
            _contract.value = Resource.error(it)
        })
    }
}