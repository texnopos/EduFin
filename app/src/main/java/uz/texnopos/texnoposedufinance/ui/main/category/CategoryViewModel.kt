package uz.texnopos.texnoposedufinance.ui.main.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.texnopos.texnoposedufinance.core.RealtimeChangesResource
import uz.texnopos.texnoposedufinance.data.firebase.CategoryHelper
import uz.texnopos.texnoposedufinance.data.model.IncomeCategory
import uz.texnopos.texnoposedufinance.core.Resource
import uz.texnopos.texnoposedufinance.data.model.ExpenseCategory

class CategoryViewModel(private val helper: CategoryHelper) : ViewModel() {
    private val _addIncomeCategory: MutableLiveData<Resource<IncomeCategory>> = MutableLiveData()
    val addIncomeCategory: LiveData<Resource<IncomeCategory>>
        get() = _addIncomeCategory

    fun addIncomeCategory(name: String) {
        _addIncomeCategory.value = Resource.loading()
        helper.addIncomeCategory(name, {
            _addIncomeCategory.value = Resource.success(IncomeCategory())
        }, {
            _addIncomeCategory.value = Resource.error(it)
        }
        )
    }

    private val _addExpenseCategory: MutableLiveData<Resource<ExpenseCategory>> = MutableLiveData()
    val addExpenseCategory: LiveData<Resource<ExpenseCategory>>
        get() = _addExpenseCategory

    fun addExpenseCategory(name: String) {
        _addExpenseCategory.value = Resource.loading()
        helper.addExpenseCategory(name, {
            _addExpenseCategory.value = Resource.success(ExpenseCategory())
        }, {
            _addExpenseCategory.value = Resource.error(it)
        }
        )
    }

    private val _expenseCategory: MutableLiveData<Resource<List<ExpenseCategory>>> =
        MutableLiveData()
    val expenseCategory: LiveData<Resource<List<ExpenseCategory>>>
        get() = _expenseCategory

    fun getAllExpenseCategories() {
        _expenseCategory.value = Resource.loading()
        helper.getAllExpenseCategories(
            {
                _expenseCategory.value = Resource.success(it)
            }, { _expenseCategory.value = Resource.error(it) })
    }

    private val _incomeCategory: MutableLiveData<RealtimeChangesResource<IncomeCategory>> = MutableLiveData()
    val incomeCategory: LiveData<RealtimeChangesResource<IncomeCategory>>
        get() = _incomeCategory

    fun getAllIncomeCategories() {
        _incomeCategory.value = RealtimeChangesResource.loading()
        helper.getAllIncomeCategories(
            {
                _incomeCategory.value = RealtimeChangesResource.onAdded(it)
            },
            {
                _incomeCategory.value = RealtimeChangesResource.onModified(it)
            },
            {
                _incomeCategory.value = RealtimeChangesResource.onRemoved(it)
            },
            {
                _incomeCategory.value = RealtimeChangesResource.error(it)
            }
        )
    }

}