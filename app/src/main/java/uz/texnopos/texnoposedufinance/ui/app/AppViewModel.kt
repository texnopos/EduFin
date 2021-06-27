package uz.texnopos.texnoposedufinance.ui.app

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.texnopos.texnoposedufinance.core.Resource
import uz.texnopos.texnoposedufinance.data.model.Course
import uz.texnopos.texnoposedufinance.data.model.response.ReportResponse
import uz.texnopos.texnoposedufinance.data.model.response.SalaryResponse
import uz.texnopos.texnoposedufinance.data.retrofit.NetworkHelper

class AppViewModel(private val helper: NetworkHelper) : ViewModel() {
    private val _report: MutableLiveData<Resource<ReportResponse>> = MutableLiveData()
    val report: LiveData<Resource<ReportResponse>>
        get() = _report

    fun getReports(fromDate: Long,
                   toDate: Long) {
        _report.value = Resource.loading()
        helper.getReports(fromDate, toDate, {
            _report.value = Resource.success(it)
        }, {
            _report.value = Resource.error(it)
        })
    }

    private val _courseList: MutableLiveData<Resource<List<Course>>> = MutableLiveData()
    val courseList: LiveData<Resource<List<Course>>>
        get() = _courseList

    fun getAllCourses() {
        _courseList.value = Resource.loading()
        helper.getAllCourses(
            {
                _courseList.value = Resource.success(it)
            },
            {
                _courseList.value = Resource.error(it)
            }
        )
    }

    private val _salary: MutableLiveData<Resource<SalaryResponse>> = MutableLiveData()
    val salary: LiveData<Resource<SalaryResponse>>
        get() = _salary

    fun getSalary(fromDate: Long,
                  toDate: Long) {
        _salary.value = Resource.loading()
        helper.getSalary(fromDate, toDate, {
            _salary.value = Resource.success(it)
        }, {
            _salary.value = Resource.error(it)
        })
    }
}