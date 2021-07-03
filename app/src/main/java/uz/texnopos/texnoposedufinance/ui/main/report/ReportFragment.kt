package uz.texnopos.texnoposedufinance.ui.main.report

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment
import uz.texnopos.texnoposedufinance.core.ResourceState
import uz.texnopos.texnoposedufinance.core.extentions.onClick
import uz.texnopos.texnoposedufinance.data.model.response.MyResponse
import uz.texnopos.texnoposedufinance.databinding.FragmentReportsBinding
import uz.texnopos.texnoposedufinance.ui.app.MainActivity
import uz.texnopos.texnoposedufinance.ui.main.MainFragment
import uz.texnopos.texnoposedufinance.ui.main.group.add.CalendarDialog
import java.text.SimpleDateFormat
import java.util.*

class ReportFragment : BaseFragment(R.layout.fragment_reports) {
    private lateinit var binding: FragmentReportsBinding
    private lateinit var adapter: ViewPagerAdapter
    private lateinit var title: TextView
    private lateinit var allAmount: TextView

    @SuppressLint("SimpleDateFormat", "ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sdf = SimpleDateFormat("dd.MM.yyyy")
        val sdf2 = SimpleDateFormat("MM.yyyy")
        val calendar = Calendar.getInstance()
        var toString = sdf.format(calendar.time).toString()
        var fromString = "01.${sdf2.format(calendar.time)}"
        val day = toString.substring(0, 2)
        var toLong = calendar.timeInMillis
        var fromLong = toLong - day.toInt() * 3600 * 1000 * 24
        if ((requireActivity() as MainActivity).report.value == null) {
            (requireActivity() as MainActivity).getReport(fromLong, toLong)
        }
        adapter = ViewPagerAdapter(requireActivity().supportFragmentManager, lifecycle)
        binding = FragmentReportsBinding.bind(view)
        setUpObservers()
        title = view.findViewById(R.id.tvTitle)
        allAmount = view.findViewById(R.id.tvAmount)
        title.text = context?.getString(R.string.reports)
        binding.apply {
            to.text = context?.getString(R.string.toText, toString)
            from.text = context?.getString(R.string.fromText, fromString)
            val cal = Calendar.getInstance()
            var currentDate = Calendar.getInstance().timeInMillis
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
            vpReports.adapter = adapter
            vpReports.setPageTransformer { _, _ ->
                when (vpReports.currentItem) {
                    0 -> {
                        (requireParentFragment().requireParentFragment() as MainFragment).temp = 0
                        onExpense()
                    }
                    else -> {
                        (requireParentFragment().requireParentFragment() as MainFragment).temp = 1
                        onIncome()
                    }
                }
            }
            clExpense.onClick {
                (requireParentFragment().requireParentFragment() as MainFragment).temp = 0
                onExpense()
                vpReports.currentItem = 0
            }
            clIncome.onClick {
                (requireParentFragment().requireParentFragment() as MainFragment).temp = 1
                onIncome()
                vpReports.currentItem = 1
            }
        }
    }

    private fun onIncome() {
        binding.apply {
            icExpenses.setImageResource(R.drawable.ic_expense)
            icIncomes.setImageResource(R.drawable.ic_incomes_selected)
            clIncome.setBackgroundResource(R.drawable.shape_green)
            clExpense.setBackgroundResource(R.drawable.shape_form)
        }
    }

    private fun onExpense() {
        binding.apply {
            icExpenses.setImageResource(R.drawable.ic_expense_selected)
            icIncomes.setImageResource(R.drawable.ic_incomes)
            clExpense.setBackgroundResource(R.drawable.shape_red)
            clIncome.setBackgroundResource(R.drawable.shape_form)
        }
    }

    private fun setUpObservers() {
        binding.apply {
            (requireActivity() as MainActivity).report.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                adapter.setReport(it)
                when (it.status) {
                    ResourceState.SUCCESS -> {
                        var allIncome = 0
                        var allExpense = 0
                        val incomeList = mutableListOf<MyResponse>()
                        val expenseList = mutableListOf<MyResponse>()
                        it.data!!.incomeCategories.forEach { i ->
                            incomeList.add(i)
                        }
                        incomeList.forEach { i ->
                            var sum = 0
                            for (j in i.incomes) {
                                sum += j.amount
                            }
                            allIncome += sum
                        }
                        val allIncomeString = textFormat(allIncome.toString())
                        tvIncomeAmount.text = context?.getString(R.string.amountIncomes, allIncomeString)


                        it.data.expenseCategories.forEach { e ->
                            expenseList.add(e)
                        }
                        expenseList.forEach { e ->
                            var sum = 0
                            for (j in e.expenses) {
                                sum += j.amount
                            }
                            allExpense += sum
                        }
                        val allExpenseString = textFormat(allExpense.toString())
                        tvExpenseAmount.text = context?.getString(R.string.amountExpenses, allExpenseString)
                        val amount = textFormat((allIncome - allExpense).toString())
                        allAmount.text = context?.getString(R.string.amount, amount)
                    }
                }
            })
        }
    }
}