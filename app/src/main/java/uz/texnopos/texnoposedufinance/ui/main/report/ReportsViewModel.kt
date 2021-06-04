package uz.texnopos.texnoposedufinance.ui.main.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.texnopos.texnoposedufinance.core.Resource
import uz.texnopos.texnoposedufinance.data.firebase.ReportHelper
import uz.texnopos.texnoposedufinance.data.model.Expense
import uz.texnopos.texnoposedufinance.data.model.Income

class ReportsViewModel(private val helper: ReportHelper): ViewModel() {
    private val _income: MutableLiveData<Resource<Income>> = MutableLiveData()
        val income: LiveData<Resource<Income>>
        get() = _income
    fun addIncome(amount: Int, date: Long, createdDate: Long, category: String){
        _income.value = Resource.loading()
        helper.addIncome(amount = amount, date = date, createdDate = createdDate, category = category,
            onSuccess = {
                _income.value = Resource.success(Income())
            }, onFailure  =  {
            _income.value = Resource.error(it)
        })
    }

    private val _expense: MutableLiveData<Resource<Expense>> = MutableLiveData()
    val expense: LiveData<Resource<Expense>>
        get() = _expense
    fun addExpense(amount: Int, date: Long, createdDate: Long, category: String){
        _expense.value = Resource.loading()
        helper.addExpense(amount = amount, date = date, createdDate = createdDate, category = category,
            onSuccess = {
                _expense.value = Resource.success(Expense())
            }, onFailure  =  {
                _expense.value = Resource.error(it)
            })
    }
}