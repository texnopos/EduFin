package uz.texnopos.texnoposedufinance.ui.main.report.expense

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Pie
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment
import uz.texnopos.texnoposedufinance.core.Resource
import uz.texnopos.texnoposedufinance.core.ResourceState
import uz.texnopos.texnoposedufinance.core.extentions.visibility
import uz.texnopos.texnoposedufinance.data.AllReports
import uz.texnopos.texnoposedufinance.data.model.response.MyResponse
import uz.texnopos.texnoposedufinance.data.model.response.ReportResponse
import uz.texnopos.texnoposedufinance.databinding.ItemExpenseBinding
import uz.texnopos.texnoposedufinance.ui.main.report.income.ReportIncAdapter

class ExpenseFragment: BaseFragment(R.layout.item_expense){
    private val expenseAdapter =
        ReportExpAdapter()
    private lateinit var pie: Pie
    lateinit var binding: ItemExpenseBinding
    var allExpense = 0
    var report: MutableLiveData<Resource<ReportResponse>> = MutableLiveData()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        pie = AnyChart.pie()
        super.onViewCreated(view, savedInstanceState)
        binding = ItemExpenseBinding.bind(view)
        setUpObservers()
        binding.apply {
            rcvExpense.adapter = expenseAdapter
        }
    }
    private fun setUpObservers() {
        binding.apply {
            report.observe(viewLifecycleOwner, Observer{
                when (it.status) {
                    ResourceState.LOADING -> {
                        loading.isVisible = true
                    }
                    ResourceState.SUCCESS -> {
                        val expenseList = mutableListOf<MyResponse>()
                        val expenses = mutableListOf<AllReports>()
                        val eList: MutableList<DataEntry> = ArrayList()
                        loading.visibility(false)
                        it.data!!.expenseCategories.forEach { e ->
                            expenseList.add(e)
                        }
                        expenseList.forEach { e ->
                            var sum = 0
                            val category = e.name
                            for (j in e.expenses) {
                                sum += j.amount
                            }
                            eList.add(ValueDataEntry(category, sum))
                            expenses.add(
                                AllReports(
                                    category = category,
                                    amount = sum,
                                    trans = e.expenses.size,
                                    expenses = e.expenses
                                )
                            )
                            allExpense += sum
                        }
                        expenseAdapter.models = expenses
                        pie.data(eList)
                        pie.title(view?.context!!.getString(R.string.s_expenses))
                        expenseAnyChartView.setChart(pie)
                    }
                    ResourceState.ERROR -> {
                        loading.visibility(false)
                        toastLN(it.message)
                    }
                }
            })
        }
    }
}
