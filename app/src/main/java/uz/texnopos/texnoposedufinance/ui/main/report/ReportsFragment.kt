package uz.texnopos.texnoposedufinance.ui.main.report

import android.annotation.SuppressLint
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
import uz.texnopos.texnoposedufinance.core.extentions.onClick
import uz.texnopos.texnoposedufinance.core.extentions.visibility
import uz.texnopos.texnoposedufinance.data.AllReports
import uz.texnopos.texnoposedufinance.data.model.response.MyResponse
import uz.texnopos.texnoposedufinance.databinding.ActionBarBinding
import uz.texnopos.texnoposedufinance.databinding.ActionBarReportBinding
import uz.texnopos.texnoposedufinance.databinding.FragmentReportsBinding
import uz.texnopos.texnoposedufinance.ui.main.MainFragment
import uz.texnopos.texnoposedufinance.ui.main.group.add.CalendarDialog
import java.text.SimpleDateFormat
import java.util.*

class ReportsFragment : BaseFragment(R.layout.fragment_reports) {
    private lateinit var binding: FragmentReportsBinding
    private lateinit var actBinding: ActionBarReportBinding
    var currentDate = 0L
    var toLong = 0L
    var fromLong = 0L
    private val viewModel: ReportsViewModel by viewModel()
    lateinit var pie: Pie
    lateinit var pie2: Pie
    private val incomeAdapter = ReportsAdapter()
    private val expenseAdapter = ReportsAdapter()
    var allIncome = 0
    var allExpense = 0

    @SuppressLint("SimpleDateFormat", "ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sdf = SimpleDateFormat("dd.MM.yyyy")
        val sdf2 = SimpleDateFormat("MM.yyyy")
        val calendar = Calendar.getInstance()
        var toString = sdf.format(calendar.time).toString()
        var fromString = "01.${sdf2.format(calendar.time)}"
        val day = toString.substring(0, 2)
        toLong = calendar.timeInMillis
        fromLong = toLong - day.toInt() * 3600 * 1000 * 24
        binding = FragmentReportsBinding.bind(view)
        actBinding = ActionBarReportBinding.bind(view)
        actBinding.apply {
            tvTitle.text = context?.getString(R.string.reports)
            tvAmount.text = context?.getString(R.string.amount, allIncome - allExpense)
        }
        pie = AnyChart.pie()
        pie2 = AnyChart.pie()
        viewModel.getReports(fromLong, toLong)
        setUpObserversExpense()

        binding.apply {
            amountIncomes.text = context?.getString(R.string.amountIncomes, allIncome)
            amountExpenses.text = context?.getString(R.string.amountExpenses, allExpense)
            to.text = context?.getString(R.string.toText, toString)
            from.text = context?.getString(R.string.fromText, fromString)
            rcvExpense.adapter = expenseAdapter
            rcvIncome.adapter = incomeAdapter
            val cal = Calendar.getInstance()
            currentDate = Calendar.getInstance().timeInMillis
            if ((requireParentFragment().requireParentFragment() as MainFragment).temp == 0) {
                onExpense()
            }
            if ((requireParentFragment().requireParentFragment() as MainFragment).temp == 1) {
                onIncome()
            }
            from.onClick {
                val dialog = CalendarDialog(requireContext())
                dialog.show()
                dialog.binding.apply {
                    cvCalendar.maxDate = System.currentTimeMillis()
                    btnYes.onClick {
                        val y = cvCalendar.year
                        val m = cvCalendar.month
                        val d = cvCalendar.dayOfMonth
                        val yStr = y.toString()
                        var mStr = (m + 1).toString()
                        var dStr = d.toString()
                        if (dStr.length != 2) dStr = "0$dStr"
                        if (mStr.length != 2) mStr = "0$mStr"
                        cal.set(Calendar.DAY_OF_MONTH, d)
                        cal.set(Calendar.MONTH, m)
                        cal.set(Calendar.YEAR, y)
                        fromLong = cal.timeInMillis
                        fromString = "$dStr.$mStr.$yStr"
                        from.text = context?.getString(R.string.fromText, fromString)
                        dialog.dismiss()
                    }
                    btnCancel.onClick {
                        dialog.dismiss()
                    }
                }
            }
            to.onClick {
                val dialog = CalendarDialog(requireContext())
                dialog.show()
                dialog.binding.apply {
                    cvCalendar.maxDate = System.currentTimeMillis()
                    btnYes.onClick {
                        val y = cvCalendar.year
                        val m = cvCalendar.month
                        val d = cvCalendar.dayOfMonth
                        val yStr = y.toString()
                        var mStr = (m + 1).toString()
                        var dStr = d.toString()
                        if (dStr.length != 2) dStr = "0$dStr"
                        if (mStr.length != 2) mStr = "0$mStr"
                        cal.set(Calendar.DAY_OF_MONTH, d)
                        cal.set(Calendar.MONTH, m)
                        cal.set(Calendar.YEAR, y)
                        toLong = cal.timeInMillis
                        toString = "$dStr.$mStr.$yStr"
                        to.text = context?.getString(R.string.toText, toString)
                        dialog.dismiss()
                    }
                    btnCancel.onClick {
                        dialog.dismiss()
                    }
                }
            }

            clExpense.onClick {
                (requireParentFragment().requireParentFragment() as MainFragment).temp = 0
                onExpense()
            }
            clIncome.onClick {
                (requireParentFragment().requireParentFragment() as MainFragment).temp = 1
                onIncome()
            }
        }
    }

    private fun setUpObserversExpense() {
        val expenseList = mutableListOf<MyResponse>()
        val eList: MutableList<DataEntry> = ArrayList()
        val expenses = mutableListOf<AllReports>()

        val incomeList = mutableListOf<MyResponse>()
        val iList: MutableList<DataEntry> = ArrayList()
        val incomes = mutableListOf<AllReports>()
        binding.apply {
            viewModel.report.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                when (it.status) {
                    ResourceState.LOADING -> {

                    }
                    ResourceState.SUCCESS -> {
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
                        binding.expenseAnyChartView.setChart(pie)

                        it.data.incomeCategories.forEach { i ->
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
                        pie2.data(iList)
                        pie2.title(view?.context!!.getString(R.string.s_incomes))
                        binding.incomeAnyChartView.setChart(pie2)
                        amountIncomes.text = context?.getString(R.string.amountIncomes, allIncome)
                        amountExpenses.text = context?.getString(R.string.amountExpenses, allExpense)
                        actBinding.tvAmount.text = context?.getString(R.string.amount, allIncome - allExpense)
                    }
                    ResourceState.ERROR -> {
                        toastLN(it.message)
                    }
                }

            })
        }
    }


    private fun onIncome() {
        binding.apply {
            icExpenses.setImageResource(R.drawable.ic_expense)
            icIncomes.setImageResource(R.drawable.ic_incomes_selected)
            clIncome.setBackgroundResource(R.drawable.shape_green)
            clExpense.setBackgroundResource(R.drawable.shape_form)
            cardView2.visibility(true)
            cardView.visibility(false)
        }
    }

    private fun onExpense() {
        binding.apply {
            icExpenses.setImageResource(R.drawable.ic_expense_selected)
            icIncomes.setImageResource(R.drawable.ic_incomes)
            clExpense.setBackgroundResource(R.drawable.shape_red)
            clIncome.setBackgroundResource(R.drawable.shape_form)
            cardView.visibility(true)
            cardView2.visibility(false)
        }
    }
}