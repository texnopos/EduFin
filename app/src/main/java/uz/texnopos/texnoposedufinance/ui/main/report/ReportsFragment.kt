package uz.texnopos.texnoposedufinance.ui.main.report

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment
import uz.texnopos.texnoposedufinance.core.extentions.onClick
import uz.texnopos.texnoposedufinance.databinding.ActionBarBinding
import uz.texnopos.texnoposedufinance.databinding.FragmentReportsBinding
import uz.texnopos.texnoposedufinance.ui.main.MainFragment
import uz.texnopos.texnoposedufinance.ui.main.group.add.CalendarDialog
import java.text.SimpleDateFormat
import java.util.*


class ReportsFragment : BaseFragment(R.layout.fragment_reports) {
    private lateinit var binding: FragmentReportsBinding
    private lateinit var actBinding: ActionBarBinding
    var currentDate = 0L
    var toLong = 0L
    var fromLong = 0L

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pie = AnyChart.pie()
        val sdf = SimpleDateFormat("dd.MM.yyyy")
        val sdf2 = SimpleDateFormat("MM.yyyy")
        var toString = sdf.format(Calendar.getInstance().time).toString()
        var fromString = "01.${sdf2.format(Calendar.getInstance().time)}"
        binding = FragmentReportsBinding.bind(view)
        actBinding = ActionBarBinding.bind(view)

        actBinding.apply {
            tvTitle.text = context?.getString(R.string.reports)
        }
        binding.apply {
            to.text = context?.getString(R.string.toText, toString)
            from.text = context?.getString(R.string.fromText, fromString)
            val data: MutableList<DataEntry> = ArrayList()
            data.add(ValueDataEntry("John", 10000))
            data.add(ValueDataEntry("Jake", 12000))
            data.add(ValueDataEntry("Peter", 18000))
            pie.data(data)
            anyChartView.setChart(pie)
            val cal = Calendar.getInstance()
            currentDate = Calendar.getInstance().timeInMillis
            if((requireParentFragment().requireParentFragment() as MainFragment).temp == 0){
                clExpense.setBackgroundResource(R.drawable.shape_red)
                icExpenses.setImageResource(R.drawable.ic_expense_selected)
                icIncomes.setImageResource(R.drawable.ic_incomes)
                clIncome.setBackgroundResource(R.drawable.shape_form)
            }
            if((requireParentFragment().requireParentFragment() as MainFragment).temp == 1){
                icExpenses.setImageResource(R.drawable.ic_expense)
                icIncomes.setImageResource(R.drawable.ic_incomes_selected)
                clIncome.setBackgroundResource(R.drawable.shape_green)
                clExpense.setBackgroundResource(R.drawable.shape_form)
            }

            (requireParentFragment().requireParentFragment() as MainFragment).temp = 0
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
            if(fromLong <= toLong){
                //
            }
            else toastLNCenter(context?.getString(R.string.selectOtherDate))

            clExpense.onClick {
                icExpenses.setImageResource(R.drawable.ic_expense_selected)
                icIncomes.setImageResource(R.drawable.ic_incomes)
                clExpense.setBackgroundResource(R.drawable.shape_red)
                clIncome.setBackgroundResource(R.drawable.shape_form)
                (requireParentFragment().requireParentFragment() as MainFragment).temp = 0
            }
            clIncome.onClick {
                icExpenses.setImageResource(R.drawable.ic_expense)
                icIncomes.setImageResource(R.drawable.ic_incomes_selected)
                clIncome.setBackgroundResource(R.drawable.shape_green)
                clExpense.setBackgroundResource(R.drawable.shape_form)
                (requireParentFragment().requireParentFragment() as MainFragment).temp = 1
            }
        }
    }
}