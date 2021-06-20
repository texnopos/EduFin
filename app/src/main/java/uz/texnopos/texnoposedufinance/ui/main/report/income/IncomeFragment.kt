package uz.texnopos.texnoposedufinance.ui.main.report.income

import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Pie
import org.koin.android.viewmodel.ext.android.viewModel
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment
import uz.texnopos.texnoposedufinance.core.Resource
import uz.texnopos.texnoposedufinance.core.ResourceState
import uz.texnopos.texnoposedufinance.core.extentions.visibility
import uz.texnopos.texnoposedufinance.data.AllReports
import uz.texnopos.texnoposedufinance.data.model.response.MyResponse
import uz.texnopos.texnoposedufinance.data.model.response.ReportResponse
import uz.texnopos.texnoposedufinance.databinding.ItemIncomeBinding
import uz.texnopos.texnoposedufinance.ui.main.report.ReportsAdapter
import uz.texnopos.texnoposedufinance.ui.main.report.ReportViewModel
import java.util.*

class IncomeFragment : BaseFragment(R.layout.item_income) {
    lateinit var binding: ItemIncomeBinding
    private lateinit var pie: Pie
    private val incomeAdapter = ReportsAdapter()
    private val viewModel: ReportViewModel by viewModel()
    var allIncome = 0
    var report: MutableLiveData<Resource<ReportResponse>> = MutableLiveData()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ItemIncomeBinding.bind(view)
        pie = AnyChart.pie()
        setUpObservers()
        binding.apply {
            rcvIncome.adapter = incomeAdapter
        }
    }

    private fun setUpObservers() {
        val incomeList = mutableListOf<MyResponse>()
        val iList: MutableList<DataEntry> = ArrayList()
        val incomes = mutableListOf<AllReports>()
        binding.apply {
            report.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
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
                        amountIncomes.text = context?.getString(R.string.amountIncomes, allIncome)
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