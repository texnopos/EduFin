package uz.texnopos.texnoposedufinance.ui.main.report.income

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.texnopos.texnoposedufinance.core.Resource
import uz.texnopos.texnoposedufinance.data.firebase.IncomeHelper
import uz.texnopos.texnoposedufinance.data.model.Payment

class IncomeViewModel(private val helper: IncomeHelper): ViewModel() {
    private val _coursePayment: MutableLiveData<Resource<Payment>> = MutableLiveData()
    val coursePayment: LiveData<Resource<Payment>>
        get() = _coursePayment
    fun addPayment(amount: Int, date: String, createdDate: String, participantId: String,
                   groupId: String, courseId: String){
        _coursePayment.value = Resource.loading()
        helper.addCoursePayment(amount, date, createdDate, participantId, groupId, courseId,
            {_coursePayment.value = Resource.success(Payment())},
            {_coursePayment.value = Resource.error(it)})
    }
}