package uz.texnopos.texnoposedufinance.ui.app

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.texnopos.texnoposedufinance.core.Resource
import uz.texnopos.texnoposedufinance.data.model.response.ReportResponse
import uz.texnopos.texnoposedufinance.data.retrofit.NetworkHelper

class MainActivityViewModel(private val nHelper: NetworkHelper) : ViewModel() {
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