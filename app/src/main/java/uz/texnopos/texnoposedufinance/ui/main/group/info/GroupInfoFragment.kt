package uz.texnopos.texnoposedufinance.ui.main.group.info

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment
import uz.texnopos.texnoposedufinance.core.ResourceState
import uz.texnopos.texnoposedufinance.core.extentions.onClick
import uz.texnopos.texnoposedufinance.core.extentions.visibility
import uz.texnopos.texnoposedufinance.data.model.Course
import uz.texnopos.texnoposedufinance.data.model.CoursePayments
import uz.texnopos.texnoposedufinance.data.model.Group
import uz.texnopos.texnoposedufinance.data.model.SendParticipantDataRequest
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
    private val viewModel: GroupInfoViewModel by viewModel()
    private val safeArgs: GroupInfoFragmentArgs by navArgs()
    lateinit var groupStr: String
    lateinit var courseStr: String
    private val adapter: GroupInfoAdapter by inject()
    lateinit var group: Group
    lateinit var course: Course
    lateinit var dialog: PaymentDialog
    var date: Long = 0L
    var created: Long = 0L
    var amount = 0
    lateinit var participantId: String
    val auth: FirebaseAuth by inject()

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        groupStr = safeArgs.group
        courseStr = safeArgs.course

        val gson = Gson()
        group = gson.fromJson(groupStr, Group::class.java)
        course = gson.fromJson(courseStr, Course::class.java)

        (requireParentFragment().requireParentFragment() as MainFragment).group = groupStr
        (requireParentFragment().requireParentFragment() as MainFragment).groupId = group.id

        binding = FragmentGroupInfoBinding.bind(view)
        actBinding = ActionBarAddBinding.bind(view)
        navController = Navigation.findNavController(view)
        setUpObservers()
        adapter.periodCount = course.duration
        adapter.coursePrice = course.price

        adapter.callStudentClicked {n, n1 ->
            val dialog = AlertDialog.Builder(requireContext())
            dialog.setTitle(context?.getString(R.string.callStudent))
            dialog.setMessage(context?.getString(R.string.selectPhone))
            dialog.setNegativeButton(context?.getString(R.string.phone2)) { d, _ ->
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode(n1)))
                requireActivity().startActivity(intent)
                d.dismiss()
            }
            dialog.setPositiveButton(context?.getString(R.string.phone1)) { d, _ ->
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode(n)))
                requireActivity().startActivity(intent)
                d.dismiss()
            }
            dialog.show()
        }

        actBinding.apply {
            actionBarTitle.text = "${group.courseName}: ${group.name}"
            btnHome.onClick {
                navController.popBackStack()
            }
        }
        viewModel.getGroupParticipants(group.id)
        setUpObserversCoursePayment()
        adapter.setOnStudentItemClickListener { pId ->
            participantId = pId
            dialog = PaymentDialog(requireContext())
            dialog.show()
            val cal = Calendar.getInstance()
            created = cal.timeInMillis
            dialog.binding.btnYes.onClick {
                val d = dialog.binding.dpDate.dayOfMonth
                val m = dialog.binding.dpDate.month
                val y = dialog.binding.dpDate.year
                cal.set(Calendar.DAY_OF_MONTH, d)
                cal.set(Calendar.MONTH, m)
                cal.set(Calendar.YEAR, y)
                date = cal.timeInMillis
                if (dialog.binding.etPayment.text.toString().isNotEmpty()) {
                    amount = dialog.binding.etPayment.text.toString().toInt()
                    val id = UUID.randomUUID().toString()
                    if (amount > 0) {
                        viewModel.coursePayment(CoursePayments(id, amount, date, created, participantId, group.id, group.courseId, auth.currentUser!!.uid))
                        }
                    else dialog.dismiss()
                } else {
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
    }

    private fun setUpObserversCoursePayment() {
        binding.apply {
            viewModel.coursePayment.observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    ResourceState.LOADING -> {
                        isLoadingDialog(true)
                        srlStudents.isRefreshing = false
                    }
                    ResourceState.SUCCESS -> {
                        dialog.dismiss()
                        isLoadingDialog(false)
                        srlStudents.isRefreshing = false
                        viewModel.getGroupParticipants(group.id)
                        toastLN(context?.getString(R.string.added_successfully))
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

    private fun isLoadingDialog(b: Boolean) {
        dialog.binding.apply {
            btnYes.isEnabled = !b
            btnCancel.isEnabled = !b
            dpDate.isEnabled = !b
            etPayment.isEnabled = !b
        }
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
                        if (it.data!!.isNotEmpty()) {
                            srlStudents.visibility(true)
                            rcvStudents.visibility(true)
                            adapter.models = it.data
                        } else {
                            srlStudents.visibility(false)
                            rcvStudents.visibility(false)
                            tvEmptyList.visibility(true)
                        }
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