package uz.texnopos.texnoposedufinance.ui.main.report.expense

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Pie
import org.koin.android.viewmodel.ext.android.viewModel
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment
import uz.texnopos.texnoposedufinance.core.ResourceState
import uz.texnopos.texnoposedufinance.core.extentions.visibility
import uz.texnopos.texnoposedufinance.data.AllReports
import uz.texnopos.texnoposedufinance.data.model.response.MyResponse
import uz.texnopos.texnoposedufinance.databinding.ItemExpenseBinding
import uz.texnopos.texnoposedufinance.ui.main.report.ReportsAdapter
import uz.texnopos.texnoposedufinance.ui.main.report.ReportsFragment
import uz.texnopos.texnoposedufinance.ui.main.report.ReportsViewModel

class ExpenseFragment(private val fromDate: Long, private val toDate: Long, private val fr: ReportsFragment): BaseFragment(R.layout.item_expense){
    private val expenseAdapter = ReportsAdapter()
    private lateinit var pie: Pie
    private val viewModel: ReportsViewModel by viewModel()
    lateinit var binding: ItemExpenseBinding
    var allExpense = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        pie = AnyChart.pie()
        super.onViewCreated(view, savedInstanceState)
        binding = ItemExpenseBinding.bind(view)
        binding.apply {
            rcvExpense.adapter = expenseAdapter
        }
        setUpObservers()
        viewModel.getReports(fromDate, toDate)
    }
    private fun setUpObservers() {
        val expenseList = mutableListOf<MyResponse>()
        val eList: MutableList<DataEntry> = ArrayList()
        val expenses = mutableListOf<AllReports>()
        binding.apply {
            viewModel.report.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                when (it.status) {
                    ResourceState.LOADING -> {
                        loading.visibility(true)
                    }
                    ResourceState.SUCCESS -> {
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
                                    trans = e.expenses.size
                                )
                            )
                            allExpense += sum
                        }
                        expenseAdapter.models = expenses
                        pie.data(eList)
                        pie.title(view?.context!!.getString(R.string.s_expenses))
                        expenseAnyChartView.setChart(pie)
                        fr.allExpense = allExpense
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
