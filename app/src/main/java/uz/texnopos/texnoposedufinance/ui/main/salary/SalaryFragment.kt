package uz.texnopos.texnoposedufinance.ui.main.salary

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment
import uz.texnopos.texnoposedufinance.core.ResourceState
import uz.texnopos.texnoposedufinance.core.extentions.enabled
import uz.texnopos.texnoposedufinance.core.extentions.onClick
import uz.texnopos.texnoposedufinance.core.extentions.visibility
import uz.texnopos.texnoposedufinance.data.model.CoursePayments
import uz.texnopos.texnoposedufinance.data.model.response.EmployeeResponse
import uz.texnopos.texnoposedufinance.data.model.response.ExpenseRequest
import uz.texnopos.texnoposedufinance.databinding.ActionBarAddBinding
import uz.texnopos.texnoposedufinance.databinding.ActionBarBinding
import uz.texnopos.texnoposedufinance.databinding.FragmentSalaryBinding
import uz.texnopos.texnoposedufinance.ui.app.MainActivity
import uz.texnopos.texnoposedufinance.ui.main.group.add.CalendarDialog
import uz.texnopos.texnoposedufinance.ui.main.group.info.PaymentDialog
import uz.texnopos.texnoposedufinance.ui.main.report.ReportViewModel
import java.text.SimpleDateFormat
import java.util.*

class SalaryFragment: BaseFragment(R.layout.fragment_salary) {
    private lateinit var binding: FragmentSalaryBinding
    private lateinit var actBinding: ActionBarBinding
    private val viewModel: ReportViewModel by viewModel()
    private var toLong = 0L
    private var fromLong = 0L
    private val adapter = SalaryAdapter()
    private var date = 0L
    private var amount = 0
    private var created = 0L
    private var teacherId = ""
    private val auth: FirebaseAuth by inject()
    private lateinit var sDialog: SalaryDialog
    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sdf = SimpleDateFormat("dd.MM.yyyy")
        val sdf2 = SimpleDateFormat("MM.yyyy")

        val calendar = Calendar.getInstance()

        var toString = sdf.format(calendar.time).toString()
        var fromString = "01.${sdf2.format(calendar.time)}"
        val day = toString.substring(0, 2)
        actBinding = ActionBarBinding.bind(view)
        binding = FragmentSalaryBinding.bind(view)
        toLong = calendar.timeInMillis
        fromLong = toLong - day.toInt() * 3600 * 1000 * 24
        if((requireActivity() as MainActivity).salary.value == null){
            (requireActivity() as MainActivity).getSalary(fromLong, toLong)
        }
        setUpObservers()
        actBinding.apply {
            tvTitle.text = context?.getString(R.string.salary_x)
        }

        binding.apply {
            to.text = context?.getString(R.string.toText, toString)
            from.text = context?.getString(R.string.fromText, fromString)
            created = calendar.timeInMillis
            rcvEmployees.adapter = adapter
            rcvEmployees.addItemDecoration(
                DividerItemDecoration(root.context, DividerItemDecoration.VERTICAL)
            )
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
            srlEmployees.setOnRefreshListener {
                isLoading(false)
                loading.visibility(false)
                (requireActivity() as MainActivity).getSalary(fromLong, toLong)
            }

            adapter.setOnItemClickListener { tId ->
                teacherId = tId
                sDialog = SalaryDialog(requireContext())
                sDialog.show()
                sDialog.binding.apply {
                    btnYes.onClick {
                        val d = dpDate.dayOfMonth
                        val m = dpDate.month
                        val y = dpDate.year
                        calendar.set(Calendar.DAY_OF_MONTH, d)
                        calendar.set(Calendar.MONTH, m)
                        calendar.set(Calendar.YEAR, y)
                        date = calendar.timeInMillis
                        if (etPayment.text.toString().isNotEmpty()) {
                            amount = etPayment.text.toString().toInt()
                            val note = etNote.text.toString()
                            val id = UUID.randomUUID().toString()
                            setUpObserversDialog()
                            if (amount > 0) {
                                viewModel.addExpense(ExpenseRequest(id = id, amount = amount, category = context?.getString(R.string.salary)!!,
                                    date = date, createdDate = created, employeeId = teacherId, note = note, orgId = auth.currentUser!!.uid))
                            } else sDialog.dismiss()
                        } else {
                            etPayment.error = context?.getString(R.string.fillField)
                        }
                    }
                    btnCancel.onClick {
                        sDialog.dismiss()
                    }
                }
            }

        }
    }

    private fun setUpObservers() {
        binding.apply {
            (requireActivity() as MainActivity).salary.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                when(it.status){
                    ResourceState.LOADING ->{
                        loading.visibility(true)
                    }
                    ResourceState.SUCCESS ->{
                        isLoading(false)
                        val eSalary = mutableListOf<EmployeeResponse>()
                        it.data!!.employeeSalary.forEach { e ->
                            eSalary.add(e)
                        }
                        adapter.models = eSalary
                    }
                    ResourceState.ERROR ->{
                        isLoading(false)
                        toastLN(it.message)
                    }
                }
            })}
    }
    private fun isLoadingDialog(b: Boolean){
        sDialog.binding.apply {
            loading.visibility(b)
            etNote.enabled(!b)
            etPayment.enabled(!b)
            dpDate.enabled(!b)
            btnCancel.enabled(!b)
            btnYes.enabled(!b)
        }
    }

    private fun isLoading(b: Boolean){
        binding.apply {
            loading.visibility(b)
            srlEmployees.isRefreshing = b
        }
    }
    private fun setUpObserversDialog(){
        sDialog.binding.apply{
            viewModel.expense.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                when(it.status){
                    ResourceState.LOADING ->{
                        isLoadingDialog(true)
                    }
                    ResourceState.SUCCESS ->{
                        isLoadingDialog(false)
                        toastLN(context?.getString(R.string.added_successfully))
                        sDialog.dismiss()
                    }
                    ResourceState.ERROR ->{
                        isLoadingDialog(false)
                        toastLN(it.message)
                    }
                }
            })
        }
    }
}