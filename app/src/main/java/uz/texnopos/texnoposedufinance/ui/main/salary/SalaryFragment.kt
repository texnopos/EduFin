package uz.texnopos.texnoposedufinance.ui.main.salary

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment
import uz.texnopos.texnoposedufinance.core.extentions.onClick
import uz.texnopos.texnoposedufinance.data.model.CoursePayments
import uz.texnopos.texnoposedufinance.databinding.ActionBarAddBinding
import uz.texnopos.texnoposedufinance.databinding.ActionBarBinding
import uz.texnopos.texnoposedufinance.databinding.FragmentSalaryBinding
import uz.texnopos.texnoposedufinance.ui.main.group.add.CalendarDialog
import uz.texnopos.texnoposedufinance.ui.main.group.info.PaymentDialog
import java.text.SimpleDateFormat
import java.util.*

class SalaryFragment: BaseFragment(R.layout.fragment_salary) {
    private lateinit var binding: FragmentSalaryBinding
    private lateinit var actBinding: ActionBarBinding
    var toLong = 0L
    var fromLong = 0L
    val adapter = SalaryAdapter()
    var date = 0L
    var amount = 0
    var created = 0L
    var teacherId = ""
    @SuppressLint("SimpleDateFormat")
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

        actBinding = ActionBarBinding.bind(view)
        binding = FragmentSalaryBinding.bind(view)

        actBinding.apply {
            tvTitle.text = context?.getString(R.string.salary_x)
        }

        binding.apply {
            to.text = context?.getString(R.string.toText, toString)
            from.text = context?.getString(R.string.fromText, fromString)

            created = calendar.timeInMillis

            rcvEmployees.adapter = adapter

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
                        calendar.set(Calendar.DAY_OF_MONTH, d)
                        calendar.set(Calendar.MONTH, m)
                        calendar.set(Calendar.YEAR, y)

                        fromLong = calendar.timeInMillis
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
                        calendar.set(Calendar.DAY_OF_MONTH, d)
                        calendar.set(Calendar.MONTH, m)
                        calendar.set(Calendar.YEAR, y)

                        toLong = calendar.timeInMillis
                        toString = "$dStr.$mStr.$yStr"

                        to.text = context?.getString(R.string.toText, toString)
                        dialog.dismiss()
                    }
                    btnCancel.onClick {
                        dialog.dismiss()
                    }
                }
            }

            adapter.setOnItemClickListener { tId ->
                teacherId = tId
                val dialog = SalaryDialog(requireContext())
                dialog.show()
                dialog.binding.btnYes.onClick {
                    val d = dialog.binding.dpDate.dayOfMonth
                    val m = dialog.binding.dpDate.month
                    val y = dialog.binding.dpDate.year
                    calendar.set(Calendar.DAY_OF_MONTH, d)
                    calendar.set(Calendar.MONTH, m)
                    calendar.set(Calendar.YEAR, y)
                    date = calendar.timeInMillis
                    if (dialog.binding.etPayment.text.toString().isNotEmpty()) {
                        amount = dialog.binding.etPayment.text.toString().toInt()
                        val id = UUID.randomUUID().toString()
                        if (amount > 0) {
                            //
                        } else dialog.dismiss()
                    } else {
                        dialog.binding.etPayment.error = context?.getString(R.string.fillField)
                    }
                }
                dialog.binding.btnCancel.onClick {
                    dialog.dismiss()
                }
            }

        }
    }
}