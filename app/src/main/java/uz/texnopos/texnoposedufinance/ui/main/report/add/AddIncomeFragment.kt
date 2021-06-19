package uz.texnopos.texnoposedufinance.ui.main.report.add

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment
import uz.texnopos.texnoposedufinance.core.RealtimeChangesResourceState
import uz.texnopos.texnoposedufinance.core.ResourceState
import uz.texnopos.texnoposedufinance.core.extentions.enabled
import uz.texnopos.texnoposedufinance.core.extentions.onClick
import uz.texnopos.texnoposedufinance.core.extentions.visibility
import uz.texnopos.texnoposedufinance.data.model.CoursePayments
import uz.texnopos.texnoposedufinance.databinding.ActionBarAddBinding
import uz.texnopos.texnoposedufinance.databinding.FragmentAddIncomeBinding
import uz.texnopos.texnoposedufinance.ui.main.category.CategoryViewModel
import uz.texnopos.texnoposedufinance.ui.main.course.CourseViewModel
import uz.texnopos.texnoposedufinance.ui.main.group.add.CalendarDialog
import uz.texnopos.texnoposedufinance.ui.main.group.info.GroupInfoViewModel
import uz.texnopos.texnoposedufinance.ui.main.report.ReportsViewModel
import java.text.SimpleDateFormat
import java.util.*

class AddIncomeFragment : BaseFragment(R.layout.fragment_add_income) {
    private lateinit var binding: FragmentAddIncomeBinding
    private lateinit var navController: NavController
    private var createdDate: Long = 0
    private var time: Long = 0
    private var note = ""
    private var category: String = ""
    private val viewModel: ReportsViewModel by viewModel()
    private val categoryVM: CategoryViewModel by viewModel()
    private val courseVM: CourseViewModel by viewModel()
    private val groupInfoVM: GroupInfoViewModel by viewModel()
    private val allCategory = mutableListOf<String>()
    private val allCourse = mutableListOf<String>()
    private val allGroup = mutableListOf<String>()
    private val allParticipant = mutableListOf<String>()
    private var courseId = ""
    private var course = ""
    private var groupId = ""
    private var group = ""
    private var participantId = ""
    private var participant = ""
    var position = 0
    var size = 0
    var pSize = 0
    private val auth: FirebaseAuth by inject()
    private lateinit var categoryAdapter: ArrayAdapter<String>
    private lateinit var courseAdapter: ArrayAdapter<String>
    private lateinit var groupAdapter: ArrayAdapter<String>
    private lateinit var participantAdapter: ArrayAdapter<String>
    private lateinit var actBinding: ActionBarAddBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddIncomeBinding.bind(view)
        navController = Navigation.findNavController(view)
        actBinding = ActionBarAddBinding.bind(view)
        setUpObservers()
        categoryAdapter = ArrayAdapter(requireContext(), R.layout.item_spinner, allCategory)
        courseAdapter = ArrayAdapter(requireContext(), R.layout.item_spinner, allCourse)
        participantAdapter = ArrayAdapter(requireContext(), R.layout.item_spinner, allParticipant)
        groupAdapter = ArrayAdapter(requireContext(), R.layout.item_spinner, allGroup)
        val sdf = SimpleDateFormat("dd.MM.yyyy")
        actBinding.apply {
            btnHome.onClick {
                navController.popBackStack()
            }
            actionBarTitle.text = context?.getString(R.string.addIncome)
        }
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
                        val cal = Calendar.getInstance()
                        cal.set(Calendar.DAY_OF_MONTH, d)
                        cal.set(Calendar.MONTH, m)
                        cal.set(Calendar.YEAR, y)
                        time = cal.timeInMillis
                        etTime.setText("$dStr.$mStr.$yStr")
                        dialog.dismiss()
                    }
                    btnCancel.onClick {
                        dialog.dismiss()
                    }
                }
            }
            categoryVM.getAllIncomeCategories()
            categoryAdapter.clear()
            actCategory.setAdapter(categoryAdapter)
            actCategory.setOnFocusChangeListener { _, _ ->
                actCategory.showDropDown()
            }
            actCategory.setOnItemClickListener { adapterView, _, i, _ ->
                if (adapterView.getItemAtPosition(i)
                        .toString() != view.context.getString(R.string.doNotSelected)
                ) {
                    category = adapterView.getItemAtPosition(i).toString()
                    if (category == context?.getString(R.string.course_pay) || category == context?.getString(R.string.course_pay_min)) {
                        tilCourse.visibility(true)
                        tilGroup.visibility(true)
                        tilParticipant.visibility(true)
                        tilCourse.enabled(true)
                        tilGroup.enabled(true)
                        tilParticipant.enabled(true)
                        courseVM.getAllCourses()
                        courseAdapter.clear()
                        actCourse.setAdapter(courseAdapter)
                        actCourse.setOnItemClickListener { adapterView, _, i, _ ->
                            if (adapterView.getItemAtPosition(i)
                                    .toString() != view.context.getString(R.string.doNotSelected)
                            ) {
                                course = courseVM.courseList.value?.data!![i].name
                                courseId = courseVM.courseList.value?.data!![i].id
                                size = courseVM.courseList.value?.data!![i].groups.size
                                position = i
                                tilGroup.visibility(true)
                                if (size <= 0) {
                                    tilGroup.isEnabled = false
                                    tilParticipant.isEnabled = false
                                    group = ""
                                    groupId = ""
                                    participant = ""
                                    participantId = ""
                                } else {
                                    tilGroup.isEnabled = true
                                    tilParticipant.isEnabled = true
                                    groupAdapter.clear()
                                    actGroup.setAdapter(groupAdapter)
                                    courseVM.courseList.value?.data!![i].groups.forEach { i ->
                                        groupAdapter.add(i.name)
                                    }
                                    actGroup.setOnItemClickListener { adapterView, _, i, _ ->
                                        if (adapterView.getItemAtPosition(i)
                                                .toString() != view.context.getString(R.string.doNotSelected)
                                        ) {
                                            group = courseVM.courseList.value?.data!![position].groups[i].name
                                            groupId = courseVM.courseList.value?.data!![position].groups[i].id
                                            tilParticipant.visibility(true)
                                            participantAdapter.clear()
                                            groupInfoVM.getGroupParticipants(groupId)
                                            actParticipant.setAdapter(participantAdapter)
                                            actParticipant.setOnItemClickListener { adapterView, _, i, _ ->
                                                if (adapterView.getItemAtPosition(i)
                                                        .toString() != view.context.getString(R.string.doNotSelected)
                                                ) {
                                                    participant =
                                                        groupInfoVM.participantList.value?.data!![i].student.name
                                                    participantId =
                                                        groupInfoVM.participantList.value?.data!![i].participant.id
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        tilCourse.visibility(false)
                        tilGroup.visibility(false)
                        tilParticipant.visibility(false)
                    }
                }
            }
            btnSave.onClick {
                val amount = etAmount.text.toString()
                note = etNote.text.toString()
                if (amount.isNotEmpty() && category != context?.getString(R.string.doNotSelected) && category.isNotEmpty() &&
                    time != 0L && courseId.isNullOrEmpty() && groupId.isNullOrEmpty() && participantId.isNullOrEmpty()
                ) {
                    viewModel.addIncome(
                        amount = amount.toInt(),
                        note = note,
                        category = category,
                        createdDate = createdDate,
                        date = time
                    )
                } else if (amount.isNotEmpty() && category != context?.getString(R.string.doNotSelected) && category.isNotEmpty() &&
                    time != 0L && courseId.isNotEmpty() && groupId.isNotEmpty() && participantId.isNotEmpty()
                ) {
                    val id = UUID.randomUUID().toString()
                    groupInfoVM.coursePayment(
                        CoursePayments(
                            id = id,
                            amount = amount.toInt(),
                            date = time,
                            createdDate = createdDate,
                            participantId = participantId,
                            groupId = groupId,
                            courseId = courseId,
                            orgId = auth.currentUser!!.uid,
                            category = category
                        )
                    )
                } else {
                    if (amount.isEmpty()) etAmount.error = context?.getString(R.string.fillField)
                    if (time == 0L) toastLNCenter(context?.getString(R.string.doNotSelectedTime))
                    if (category == context?.getString(R.string.doNotSelected) && category.isEmpty())
                        toastLNCenter(context?.getString(R.string.doNotSelectedCategory))
                }
            }
        }
    }

    private fun setUpObservers() {
        binding.apply {
            viewModel.income.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
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

            groupInfoVM.coursePayment.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
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
                        isLoading(false)
                        toastLN(it.message)
                    }
                }
            }
            )

            courseVM.courseList.observe(viewLifecycleOwner,
                androidx.lifecycle.Observer {
                    when (it.status) {
                        ResourceState.SUCCESS -> {
                            courseAdapter.addAll(it.data!!.map { e -> e.name })
                        }
                        ResourceState.ERROR -> {
                            toastLN(it.message)
                        }
                    }
                })

            groupInfoVM.participantList.observe(
                viewLifecycleOwner, androidx.lifecycle.Observer {
                    when (it.status) {
                        ResourceState.SUCCESS -> {
                            val map = it.data!!.map { e -> e.student.name }
                            pSize = map.size
                            participantAdapter.addAll(map)
                        }
                        ResourceState.ERROR -> {
                            toastLN(it.message)
                        }
                        ResourceState.LOADING -> {

                        }
                    }
                })

            categoryVM.incomeCategory.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                when (it.status) {
                    RealtimeChangesResourceState.LOADING -> {
                        loading.visibility(true)
                    }
                    RealtimeChangesResourceState.ADDED -> {
                        loading.visibility(false)
                        categoryAdapter.add(it.data!!.name)
                    }
                    RealtimeChangesResourceState.ERROR -> {
                        loading.visibility(false)
                        toastLN(it.message)
                    }
                    else -> {
                    }
                }
            })
        }
    }

    fun isLoading(b: Boolean) {
        binding.apply {
            if (b) {
                tilAmount.enabled(!b)
                loading.visibility(b)
                tilNote.enabled(!b)
                tilTime.enabled(!b)
                tilCategory.enabled(!b)
                btnSave.enabled(!b)
                tilCourse.enabled(!b)
                tilGroup.enabled(!b)
                tilParticipant.enabled(!b)
            } else {
                tilAmount.enabled(b)
                loading.visibility(!b)
                tilNote.enabled(b)
                tilTime.enabled(b)
                tilCategory.enabled(b)
                btnSave.enabled(b)
                tilCourse.enabled(b)
                tilGroup.enabled(b)
                tilParticipant.enabled(b)
            }
        }
    }
}
