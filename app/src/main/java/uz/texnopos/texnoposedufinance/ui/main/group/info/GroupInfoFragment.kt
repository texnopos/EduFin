package uz.texnopos.texnoposedufinance.ui.main.group.info

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.google.gson.Gson
import org.koin.android.ext.android.inject
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment
import uz.texnopos.texnoposedufinance.core.ResourceState
import uz.texnopos.texnoposedufinance.core.extentions.onClick
import uz.texnopos.texnoposedufinance.core.extentions.visibility
import uz.texnopos.texnoposedufinance.data.model.Course
import uz.texnopos.texnoposedufinance.data.model.Group
import uz.texnopos.texnoposedufinance.databinding.ActionBarAddBinding
import uz.texnopos.texnoposedufinance.databinding.FragmentGroupInfoBinding
import uz.texnopos.texnoposedufinance.ui.main.MainFragment
import uz.texnopos.texnoposedufinance.ui.main.report.income.PaymentDialog
import java.text.SimpleDateFormat
import java.util.*

class GroupInfoFragment : BaseFragment(R.layout.fragment_group_info) {
    private lateinit var binding: FragmentGroupInfoBinding
    private lateinit var actBinding: ActionBarAddBinding
    private lateinit var navController: NavController
    private val viewModel: GroupInfoViewModel by inject()
    private val safeArgs: GroupInfoFragmentArgs by navArgs()
    lateinit var groupStr: String
    lateinit var courseStr: String
    private val adapter = GroupInfoAdapter()
    lateinit var group: Group
    lateinit var course: Course
    lateinit var dialog: PaymentDialog
    var date: String = ""
    var created = ""
    var amount = 0

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        groupStr = safeArgs.group
        courseStr = safeArgs.course

        val gson = Gson()
        group = gson.fromJson(groupStr, Group::class.java)
        course = gson.fromJson(courseStr, Course::class.java)

        (requireParentFragment().requireParentFragment() as MainFragment).groupId = group.id
        (requireParentFragment().requireParentFragment() as MainFragment).courseId = group.courseId

        binding = FragmentGroupInfoBinding.bind(view)
        actBinding = ActionBarAddBinding.bind(view)
        navController = Navigation.findNavController(view)

        adapter.periodCount = course.duration
        adapter.coursePrice = course.price

        actBinding.apply {
            actionBarTitle.text = "${group.courseName}: ${group.name}"
            btnHome.onClick {
                navController.popBackStack()
            }
        }
        adapter.setOnStudentItemClickListener { participantId ->
            dialog = PaymentDialog(requireContext())
            dialog.show()
            val sdf = SimpleDateFormat("dd.MM.yyyy hh:mm:ss")
            created = sdf.format(Calendar.getInstance().time).toString()
            dialog.binding.btnYes.onClick {
                var d = dialog.binding.dpDate.dayOfMonth.toString()
                var m = dialog.binding.dpDate.month.toString()
                var y = dialog.binding.dpDate.year.toString()
                if (y.toInt() < 10) y = "0$y"
                if (m.toInt() < 10) m = "0$m"
                if (d.toInt() < 10) d = "0$d"
                date = "$d.$m.$y"
                if(dialog.binding.etPayment.text.toString().isNotEmpty()){
                    amount = dialog.binding.etPayment.text.toString().toInt()
                    if (amount > 0) {
                        viewModel.addPayment(amount, date, created, participantId, group.id, group.courseId)
                    }
                    else dialog.dismiss()
                }
                 else {
                    dialog.binding.etPayment.error = context?.getString(R.string.fillField)
                }
            }
            dialog.binding.btnCancel.onClick {
                dialog.dismiss()
            }
        }
        binding.apply {
            rcvStudents.adapter = adapter
            tvName.text = context?.getString(R.string.teacherInfo, group.teacher)
            tvDays.text = context?.getString(R.string.daysInfo, "${group.days} / ${group.time}")
            tvStart.text = context?.getString(R.string.startInfo, group.startDate)
            srlStudents.setOnRefreshListener {
                viewModel.getGroupParticipants(group.id)
                loading.visibility(false)
            }
        }
        setUpObservers()
        viewModel.getGroupParticipants(group.id)
        setUpObserversAddPayment()
    }

    private fun setUpObserversAddPayment() {
        binding.apply {
            viewModel.coursePayment.observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    ResourceState.LOADING -> {
                        isLoadingDialog(true)
                        srlStudents.isRefreshing = false
                    }
                    ResourceState.SUCCESS -> {
                        isLoadingDialog(false)
                        srlStudents.isRefreshing = false
                        toastLN(context?.getString(R.string.added_successfully))
                        dialog.dismiss()
                    }
                    ResourceState.ERROR -> {
                        isLoadingDialog(false)
                        loading.visibility(false)
                        srlStudents.isRefreshing = false
                        toastLN(it.message)
                    }
                }
            }
            )
        }
    }
    fun isLoadingDialog(b: Boolean){
        dialog.binding.apply {

        }
        dialog.binding.btnYes.isEnabled = !b
        dialog.binding.btnCancel.isEnabled = !b
        dialog.binding.dpDate.isEnabled = !b
        dialog.binding.etPayment.isEnabled = !b
        binding.loading.visibility(b)
    }

    private fun setUpObservers() {
        binding.apply {
            viewModel.participantList.observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    ResourceState.LOADING -> {
                        loading.visibility(true)
                        srlStudents.isRefreshing = false
                    }
                    ResourceState.SUCCESS -> {
                        loading.visibility(false)
                        adapter.models = it.data!!
                        srlStudents.isRefreshing = false
                    }
                    ResourceState.ERROR -> {
                        toastLN(it.message)
                        loading.visibility(false)
                        srlStudents.isRefreshing = false
                    }
                }
            })
        }
    }
}