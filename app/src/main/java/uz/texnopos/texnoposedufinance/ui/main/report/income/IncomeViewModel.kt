package uz.texnopos.texnoposedufinance.ui.main.report.income

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.texnopos.texnoposedufinance.core.Resource
import uz.texnopos.texnoposedufinance.data.firebase.IncomeHelper
import uz.texnopos.texnoposedufinance.data.model.CoursePayments

class IncomeViewModel(private val helper: IncomeHelper): ViewModel() {
    private val _coursePayment: MutableLiveData<Resource<CoursePayments>> = MutableLiveData()
    val coursePayment: LiveData<Resource<CoursePayments>>
        get() = _coursePayment
    fun addPayment(amount: Int, date: Long, createdDate: Long, participantId: String,
                   groupId: String, courseId: String){
        _coursePayment.value = Resource.loading()
        /*helper.addCoursePaymentToIncome(amount, date, createdDate, participantId, groupId, courseId,
            {_coursePayment.value = Resource.success(CoursePayments())},
            {_coursePayment.value = Resource.error(it)})*/
    }
}