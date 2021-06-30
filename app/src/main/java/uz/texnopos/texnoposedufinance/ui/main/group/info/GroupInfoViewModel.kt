package uz.texnopos.texnoposedufinance.ui.main.group.info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.texnopos.texnoposedufinance.core.Resource
import uz.texnopos.texnoposedufinance.data.firebase.StudentHelper
import uz.texnopos.texnoposedufinance.data.model.response.ParticipantResponse
import uz.texnopos.texnoposedufinance.data.model.CoursePayments
import uz.texnopos.texnoposedufinance.data.model.Student
import uz.texnopos.texnoposedufinance.data.retrofit.NetworkHelper

class GroupInfoViewModel(private val helper: NetworkHelper) : ViewModel() {
    private val _participantList: MutableLiveData<Resource<List<ParticipantResponse>>> =
        MutableLiveData()
    val participantList: LiveData<Resource<List<ParticipantResponse>>>
        get() = _participantList

    fun getGroupParticipants(id: String) {
        _participantList.value = Resource.loading()
        helper.getGroupParticipants(id, {
            _participantList.value = Resource.success(it)
        }, {
            _participantList.value = Resource.error(it)
        })
    }
}