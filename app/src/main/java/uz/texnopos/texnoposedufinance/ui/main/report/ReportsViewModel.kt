package uz.texnopos.texnoposedufinance.ui.main.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.texnopos.texnoposedufinance.core.Resource
import uz.texnopos.texnoposedufinance.data.firebase.ReportHelper
import uz.texnopos.texnoposedufinance.data.model.Report
import uz.texnopos.texnoposedufinance.data.model.response.ReportResponse
import uz.texnopos.texnoposedufinance.data.retrofit.NetworkHelper

class ReportsViewModel(private val helper: ReportHelper, private val nHelper: NetworkHelper): ViewModel() {
    private val _income: MutableLiveData<Resource<Report>> = MutableLiveData()
        val income: LiveData<Resource<Report>>
        get() = _income
    fun addIncome(amount: Int, date: Long, createdDate: Long, category: String, note: String){
        _income.value = Resource.loading()
        helper.addIncome(amount = amount, date = date, createdDate = createdDate, category = category, note = note,
            onSuccess = {
                _income.value = Resource.success(Report())
            }, onFailure  =  {
            _income.value = Resource.error(it)
        })
    }

    private val _expense: MutableLiveData<Resource<Report>> = MutableLiveData()
    val expense: LiveData<Resource<Report>>
        get() = _expense
    fun addExpense(amount: Int, date: Long, createdDate: Long, category: String, note: String){
        _expense.value = Resource.loading()
        helper.addExpense(amount = amount, date = date, createdDate = createdDate, category = category, note = note,
            onSuccess = {
                _expense.value = Resource.success(Report())
            }, onFailure  =  {
                _expense.value = Resource.error(it)
            })
    }

    private val _report: MutableLiveData<Resource<ReportResponse>> = MutableLiveData()
    val report: LiveData<Resource<ReportResponse>>
        get() = _report

    fun getReports(fromDate: Long,
                   toDate: Long) {
        _report.value = Resource.loading()
        nHelper.getReports(fromDate, toDate, {
            _report.value = Resource.success(it)
        }, {
            _report.value = Resource.error(it)
        })
    }
}