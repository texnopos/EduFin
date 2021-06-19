package uz.texnopos.texnoposedufinance.ui.main.report.income

import android.os.Bundle
import android.view.View
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
import uz.texnopos.texnoposedufinance.databinding.ItemIncomeBinding
import uz.texnopos.texnoposedufinance.ui.main.report.ReportsAdapter
import uz.texnopos.texnoposedufinance.ui.main.report.ReportsFragment
import uz.texnopos.texnoposedufinance.ui.main.report.ReportsViewModel
import java.util.*

class IncomeFragment(
    private val fromDate: Long,
    private val toDate: Long,
    val fr: ReportsFragment
) : BaseFragment(R.layout.item_income) {
    lateinit var binding: ItemIncomeBinding
    private lateinit var pie: Pie
    private val incomeAdapter = ReportsAdapter()
    private val viewModel: ReportsViewModel by viewModel()
    var allIncome = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ItemIncomeBinding.bind(view)
        pie = AnyChart.pie()
        binding.apply {
            rcvIncome.adapter = incomeAdapter
        }
        setUpObservers()
        viewModel.getReports(fromDate, toDate)
    }

    private fun setUpObservers() {
        val incomeList = mutableListOf<MyResponse>()
        val iList: MutableList<DataEntry> = ArrayList()
        val incomes = mutableListOf<AllReports>()
        binding.apply {
            viewModel.report.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                when (it.status) {
                    ResourceState.LOADING -> {
                        incomeLoading.visibility(true)
                    }
                    ResourceState.SUCCESS -> {
                        incomeLoading.visibility(false)
                        it.data!!.incomeCategories.forEach { i ->
                            incomeList.add(i)
                        }
                        incomeList.forEach { i ->
                            var sum = 0
                            val category = i.name
                            for (j in i.incomes) {
                                sum += j.amount
                            }
                            iList.add(ValueDataEntry(category, sum))
                            incomes.add(
                                AllReports(
                                    category = category,
                                    amount = sum,
                                    trans = i.incomes.size
                                )
                            )
                            allIncome += sum
                        }
                        incomeAdapter.models = incomes
                        pie.data(iList)
                        pie.title(view?.context!!.getString(R.string.s_incomes))
                        incomeAnyChartView.setChart(pie)
                        fr.allIncome = allIncome
                    }
                    ResourceState.ERROR -> {
                        incomeLoading.visibility(false)
                        toastLN(it.message)
                    }
                }

            })
        }
    }
}