package uz.texnopos.texnoposedufinance.ui.main.student.add.select_existing_student

import android.app.AlertDialog
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
import uz.texnopos.texnoposedufinance.data.model.SendParticipantDataRequest
import uz.texnopos.texnoposedufinance.data.model.Student
import uz.texnopos.texnoposedufinance.databinding.ActionBarAddBinding
import uz.texnopos.texnoposedufinance.databinding.FragmentSelectExistingStudentBinding
import uz.texnopos.texnoposedufinance.ui.main.student.add.CreateStudentViewModel
import java.util.*

class SelectExistingStudentFragment : BaseFragment(R.layout.fragment_select_existing_student) {
    private val adapter: SelectExistingStudentAdapter by inject()
    private val viewModel: CreateStudentViewModel by viewModel()
    lateinit var binding: FragmentSelectExistingStudentBinding
    lateinit var actBinding: ActionBarAddBinding
    lateinit var navController: NavController
    private val args: SelectExistingStudentFragmentArgs by navArgs()
    lateinit var newParticipant: SendParticipantDataRequest
    private val auth: FirebaseAuth by inject()
    private lateinit var studentArgs: SendParticipantDataRequest
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSelectExistingStudentBinding.bind(view)
        actBinding = ActionBarAddBinding.bind(view)
        navController = Navigation.findNavController(view)
        val orgId = auth.currentUser!!.uid
        setUpObservers()
        val gson = Gson()
        studentArgs = gson.fromJson(args.student, SendParticipantDataRequest::class.java)
        viewModel.getStudentByPassport(studentArgs.passport)
        actBinding.apply {
            actionBarTitle.text = context?.getString(R.string.selectStudents)
            btnHome.onClick {
                navController.popBackStack()

            }
        }
        setUpObserversCreateParticipant()
        binding.apply {
            rcvStudents.adapter = adapter
            srlStudents.setOnRefreshListener {
                loading.visibility(false)
                viewModel.getStudentByPassport(studentArgs.passport)
            }
        }
        adapter.setOnItemClickListener { s ->
            val student = gson.fromJson(s, Student::class.java)
            val dialog = AlertDialog.Builder(requireContext())
            dialog.setTitle(context?.getString(R.string.addStudent))
            dialog.setMessage(context?.getString(R.string.sureToAddNewParticipant))
            dialog.setNegativeButton(context?.getString(R.string.cancel)) { d, _ ->
                d.dismiss()
            }
            dialog.setPositiveButton(context?.getString(R.string.add)) { d, _ ->
                val id = UUID.randomUUID().toString()
                newParticipant = SendParticipantDataRequest(
                    id,
                    student.id,
                    studentArgs.groupId,
                    studentArgs.courseId,
                    orgId,
                    studentArgs.passport,
                    studentArgs.contract,
                    studentArgs.phone
                )
                viewModel.createParticipantWithStudentId(newParticipant)
                d.dismiss()
            }
            dialog.show()
        }
    }

    fun setUpObservers() {
        binding.apply {
            viewModel.studentsList.observe(viewLifecycleOwner, Observer {
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

    private fun setUpObserversCreateParticipant() {
        binding.apply {
            viewModel.createParticipantWithStudentId.observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    ResourceState.LOADING -> {
                        loading.visibility(true)
                        srlStudents.isRefreshing = false
                    }
                    ResourceState.SUCCESS -> {
                        loading.visibility(false)
                        srlStudents.isRefreshing = false
                        if (it.data == "contract exists") {
                            toastLN(context?.getString(R.string.contractExists))
                        }
                        if (it.data == "success") {
                            toastLN(context?.getString(R.string.added_successfully))
                            navController.popBackStack()
                        }
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