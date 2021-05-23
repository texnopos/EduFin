package uz.texnopos.texnoposedufinance.ui.main.student.select

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment
import uz.texnopos.texnoposedufinance.core.ResourceState
import uz.texnopos.texnoposedufinance.core.extentions.onClick
import uz.texnopos.texnoposedufinance.core.extentions.visibility
import uz.texnopos.texnoposedufinance.data.model.*
import uz.texnopos.texnoposedufinance.databinding.FragmentSelectStudentsBinding
import uz.texnopos.texnoposedufinance.databinding.SelectActionBarBinding
import uz.texnopos.texnoposedufinance.ui.main.student.StudentsViewModel
import uz.texnopos.texnoposedufinance.ui.main.student.add.CreateStudentViewModel
import java.util.*

class SelectStudentsFragment : BaseFragment(R.layout.fragment_select_students) {
    private lateinit var binding: FragmentSelectStudentsBinding
    private lateinit var actBinding: SelectActionBarBinding
    private val adapter: SelectStudentsAdapter by inject()
    private val viewModel: StudentsViewModel by viewModel()
    private val vm: CreateStudentViewModel by viewModel()
    private val args: SelectStudentsFragmentArgs by navArgs()
    private lateinit var newParticipant: SendParticipantDataRequest
    private val auth: FirebaseAuth by inject()
    private val gson = Gson()
    private lateinit var group: Group
    private lateinit var navController: NavController
    private lateinit var dialog: DialogAddContract
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSelectStudentsBinding.bind(view)
        actBinding = SelectActionBarBinding.bind(view)
        navController = Navigation.findNavController(view)
        val orgId = auth.currentUser!!.uid
        group = gson.fromJson(args.groupId, Group::class.java)
        setUpObservers()
        actBinding.apply {
            tvSelectedItem.text = context?.getString(R.string.selectStudents)
            btnHome.onClick {
                navController.popBackStack()
            }
        }
        binding.apply {
            rcvStudents.adapter = adapter
            loading.visibility(false)
            srlStudents.setOnRefreshListener {
                viewModel.selectExistingStudentToGroup(group.id)
                loading.visibility(false)
            }
            adapter.setOnItemClickListener { s ->
                val student = gson.fromJson(s, Student::class.java)
                dialog =
                    DialogAddContract(
                        requireContext()
                    )
                dialog.show()
                dialog.binding.apply {
                    btnCancel.onClick {
                        dialog.dismiss()
                    }
                    btnYes.onClick {
                        val contract = etContractNum.text.toString()
                        val id = UUID.randomUUID().toString()
                        if (contract.isNotEmpty()) {
                            val cnt = contract.toInt()
                            vm.checkContract(ContractRequest(auth.currentUser!!.uid, cnt))
                            newParticipant = SendParticipantDataRequest(
                                id,
                                student.id,
                                group.id,
                                group.courseId,
                                orgId,
                                student.passport,
                                cnt,
                                student.phone
                            )
                        } else etContractNum.error = context?.getString(R.string.fillField)
                    }
                }

            }
            rcvStudents.addItemDecoration(
                DividerItemDecoration(
                    root.context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
        viewModel.selectExistingStudentToGroup(group.id)
        setUpObserversCheck()
        setUpObserversCreateParticipant()
    }

    private fun setUpObserversCheck() {
        binding.apply {
            vm.contract.observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    ResourceState.LOADING -> {
                        isLoadingDialog(true)
                    }
                    ResourceState.SUCCESS -> {
                        isLoadingDialog(false)
                        if (it.data == "contract exists") {
                            toastLN(context?.getString(R.string.contractExists))
                            srlStudents.isRefreshing = false
                            loading.visibility(false)
                        }
                        if (it.data == "success") {
                            vm.createParticipantWithStudentId(newParticipant)
                            dialog.dismiss()
                        }
                    }
                    ResourceState.ERROR -> {
                        toastLN(it.message)
                        isLoadingDialog(false)
                    }
                }
            })
        }
    }

    private fun setUpObservers() {
        binding.apply {
            viewModel.selectStudentsList.observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    ResourceState.LOADING -> {
                        srlStudents.isRefreshing = false
                        loading.visibility(true)
                    }
                    ResourceState.SUCCESS -> {
                        srlStudents.isRefreshing = false
                        if (it.data!!.isNotEmpty()) {
                            tvEmptyList.visibility = View.GONE
                            rcvStudents.visibility = View.VISIBLE
                            adapter.models = it.data
                        } else {
                            tvEmptyList.visibility = View.VISIBLE
                            rcvStudents.visibility = View.GONE
                        }
                        loading.visibility(false)
                    }
                    ResourceState.ERROR -> {
                        srlStudents.isRefreshing = false
                        toastLN(it.message)
                        loading.visibility(false)
                    }
                }
            })
        }
    }

    private fun isLoadingDialog(b: Boolean) {
        dialog.binding.apply {
            btnCancel.isEnabled = !b
            btnYes.isEnabled = !b
            etContractNum.isEnabled = !b
            loading.visibility(b)
        }
    }

    private fun setUpObserversCreateParticipant() {
        binding.apply {
            vm.createParticipantWithStudentId.observe(viewLifecycleOwner, Observer { st ->
                when (st.status) {
                    ResourceState.LOADING -> {
                        dialog.binding.loading.visibility(true)
                    }
                    ResourceState.SUCCESS -> {
                        if (st.data == "success") {
                            vm.createParticipantWithStudentId(newParticipant)
                            toastLN(context?.getString(R.string.added_successfully))
                            viewModel.selectExistingStudentToGroup(group.id)
                            dialog.binding.loading.visibility(false)
                            dialog.dismiss()
                        }
                    }
                    ResourceState.ERROR -> {
                        dialog.binding.loading.visibility(false)
                    }
                }
            })
        }
    }
}
