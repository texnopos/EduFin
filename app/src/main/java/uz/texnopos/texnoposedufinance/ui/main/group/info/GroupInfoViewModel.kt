package uz.texnopos.texnoposedufinance.ui.main.group.info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.texnopos.texnoposedufinance.core.Resource
import uz.texnopos.texnoposedufinance.data.firebase.IncomeHelper
import uz.texnopos.texnoposedufinance.data.model.ParticipantResponse
import uz.texnopos.texnoposedufinance.data.model.Payment
import uz.texnopos.texnoposedufinance.data.network.NetworkHelper

class GroupInfoViewModel(private val helper: NetworkHelper, private val incomeHelper: IncomeHelper): ViewModel() {
    private val _participantList: MutableLiveData<Resource<List<ParticipantResponse>>> = MutableLiveData()
    val participantList:LiveData<Resource<List<ParticipantResponse>>>
        get() = _participantList
    fun getGroupParticipants(id: String){
        _participantList.value = Resource.loading()
        helper.getGroupParticipants(id, {
            _participantList.value = Resource.success(it)
        }, {
            _participantList.value = Resource.error(it)
        })
    }

    private val _coursePayment: MutableLiveData<Resource<Payment>> = MutableLiveData()
    val coursePayment: LiveData<Resource<Payment>>
        get() = _coursePayment
    fun addPayment(amount: Int, date: String, createdDate: String, participantId: String,
                   groupId: String, courseId: String){
        _coursePayment.value = Resource.loading()
        incomeHelper.addCoursePayment(amount, date, createdDate, participantId, groupId, courseId,
            {_coursePayment.value = Resource.success(Payment())},
            {_coursePayment.value = Resource.error(it)})
    }
}