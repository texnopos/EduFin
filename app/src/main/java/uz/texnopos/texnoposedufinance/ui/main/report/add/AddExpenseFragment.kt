package uz.texnopos.texnoposedufinance.ui.main.report.add


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment
import uz.texnopos.texnoposedufinance.core.ResourceState
import uz.texnopos.texnoposedufinance.core.extentions.enabled
import uz.texnopos.texnoposedufinance.core.extentions.onClick
import uz.texnopos.texnoposedufinance.core.extentions.visibility
import uz.texnopos.texnoposedufinance.data.model.response.ExpenseRequest
import uz.texnopos.texnoposedufinance.databinding.ActionBarAddBinding
import uz.texnopos.texnoposedufinance.databinding.FragmentAddExpenseBinding
import uz.texnopos.texnoposedufinance.ui.main.category.CategoryViewModel
import uz.texnopos.texnoposedufinance.ui.main.group.add.CalendarDialog
import uz.texnopos.texnoposedufinance.ui.main.report.ReportViewModel
import uz.texnopos.texnoposedufinance.ui.main.teacher.TeacherViewModel
import java.text.SimpleDateFormat
import java.util.*

class AddExpenseFragment : BaseFragment(R.layout.fragment_add_expense) {
    private lateinit var binding: FragmentAddExpenseBinding
    private lateinit var navController: NavController
    private var createdDate: Long = 0
    private var time: Long = 0
    private var note = ""
    private var category: String = ""
    private val viewModel: ReportViewModel by viewModel()
    private val ctViewModel: CategoryViewModel by viewModel()
    private val tViewModel: TeacherViewModel by viewModel()
    private lateinit var actBinding: ActionBarAddBinding
    private val allCategory = mutableListOf<String>()
    private val allEmployee = mutableListOf<String>()
    private lateinit var categoryAdapter: ArrayAdapter<String>
    private lateinit var employeeAdapter: ArrayAdapter<String>
    var employeeId = ""
    val auth: FirebaseAuth by inject()
    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddExpenseBinding.bind(view)
        actBinding = ActionBarAddBinding.bind(view)
        navController = Navigation.findNavController(view)
        val sdf = SimpleDateFormat("dd.MM.yyyy")
        actBinding.apply {
            btnHome.onClick {
                navController.popBackStack()
            }
            actionBarTitle.text = context?.getString(R.string.addExpense)

        }
        setUpObservers()
        categoryAdapter = ArrayAdapter(requireContext(), R.layout.item_spinner, allCategory)
        employeeAdapter = ArrayAdapter(requireContext(), R.layout.item_spinner, allEmployee)
        binding.apply {
            val cl = Calendar.getInstance()
            createdDate = cl.timeInMillis
            time = cl.timeInMillis
            etTime.setText(sdf.format(cl.time).toString())
            etTime.onClick {
                val dialog = CalendarDialog(requireContext())
                dialog.show()
                dialog.binding.apply {
                    btnYes.onClick {
                        val y = cvCalendar.year
                        val m = cvCalendar.month
                        val d = cvCalendar.dayOfMonth
                        val yStr = y.toString()
                        var mStr = (m + 1).toString()
                        var dStr = d.toString()
                        if (dStr.length != 2) dStr = "0$dStr"
                        if (mStr.length != 2) mStr = "0$mStr"
                        cl.set(Calendar.DAY_OF_MONTH, d)
                        cl.set(Calendar.MONTH, m)
                        cl.set(Calendar.YEAR, y)
                        time = cl.timeInMillis
                        etTime.setText("$dStr.$mStr.$yStr")
                        dialog.dismiss()
                    }
                    btnCancel.onClick {
                        dialog.dismiss()
                    }
                }
            }
            ctViewModel.getAllExpenseCategories()
            categoryAdapter.clear()
            actCategory.setAdapter(categoryAdapter)
            actCategory.setOnItemClickListener { adapterView, _, i, _ ->
                if (adapterView.getItemAtPosition(i)
                        .toString() != view.context.getString(R.string.doNotSelected)
                ) {
                    category = adapterView.getItemAtPosition(i).toString()
                    if (category == context?.getString(R.string.salary_x)) {
                        tilEmployees.visibility(true)
                        tilEmployees.isEnabled = true
                        tViewModel.getAllTeachers()
                        employeeAdapter.clear()
                        actEmployees.setAdapter(employeeAdapter)
                        actEmployees.setOnItemClickListener { adapterView, _, index, _ ->
                            if(adapterView.getItemAtPosition(index)
                                    .toString() != view.context.getString(R.string.doNotSelected)
                            ) {
                               employeeId = tViewModel.teacherList.value!!.data!![index].id
                            }
                            ctViewModel.expenseCategory.value?.data!![i].name
                        }
                    }
                    btnSave.onClick {
                        val amount = etAmount.text.toString()
                        val id = UUID.randomUUID().toString()
                        note = etNote.text.toString()
                        if (amount.isNotEmpty() && category != context?.getString(R.string.doNotSelected) && category.isNotEmpty() && time != 0L) {
                            viewModel.addExpense(
                                ExpenseRequest(amount = amount.toInt(), category = category,
                                date = time, createdDate = createdDate, id = id, employeeId = employeeId, note = note, orgId = auth.currentUser!!.uid)
                            )
                        } else {
                            if (amount.isEmpty()) etAmount.error =
                                context?.getString(R.string.fillField)
                            if (time == 0L) toastLNCenter(context?.getString(R.string.doNotSelectedTime))
                            if (category == context?.getString(R.string.doNotSelected) && category.isEmpty())
                                toastLNCenter(context?.getString(R.string.doNotSelectedCategory))
                        }
                    }
                }
            }
        }
    }

    private fun setUpObservers() {
        binding.apply {
            ctViewModel.expenseCategory.observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    ResourceState.LOADING -> loading.visibility(true)
                    ResourceState.SUCCESS -> {
                        loading.visibility(false)
                        categoryAdapter.addAll(it.data!!.map { e -> e.name })
                    }
                    ResourceState.ERROR -> {
                        loading.visibility(false)
                        toastLN(it.message)
                    }
                }
            })
            tViewModel.teacherList.observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    ResourceState.LOADING -> loading.visibility(true)
                    ResourceState.SUCCESS -> {
                        loading.visibility(false)
                        employeeAdapter.addAll(it.data!!.map { e -> e.name })
                    }
                    ResourceState.ERROR -> {
                        loading.visibility(false)
                        toastLN(it.message)
                    }
                }
            })
            viewModel.expense.observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    ResourceState.LOADING -> {
                        isLoading(true)
                    }
                    ResourceState.SUCCESS -> {
                        isLoading(false)
                        toastLN(context?.getString(R.string.added_successfully))
                        navController.popBackStack()
                    }
                    ResourceState.ERROR -> {
                        toastLN(it.message)
                        isLoading(false)
                    }
                }
            })
        }
    }

    fun isLoading(b: Boolean) {
        binding.apply {
            if (b) {
                etAmount.enabled(!b)
                loading.visibility(b)
                etNote.enabled(!b)
                etTime.enabled(!b)
                actCategory.enabled(!b)
                btnSave.enabled(!b)
            } else {
                tilAmount.enabled(b)
                loading.visibility(!b)
                tilNote.enabled(b)
                tilTime.enabled(b)
                tilCategory.enabled(b)
                btnSave.enabled(b)
            }
        }

    }
}