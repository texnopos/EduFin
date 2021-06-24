package uz.texnopos.texnoposedufinance.ui.main.student.add

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.ScrollView
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import uz.texnopos.texnoposedufinance.MainActivity
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment
import uz.texnopos.texnoposedufinance.core.ResourceState
import uz.texnopos.texnoposedufinance.core.extentions.onClick
import uz.texnopos.texnoposedufinance.core.extentions.visibility
import uz.texnopos.texnoposedufinance.data.model.CreateParticipantRequest
import uz.texnopos.texnoposedufinance.data.model.Group
import uz.texnopos.texnoposedufinance.data.model.SendParticipantDataRequest
import uz.texnopos.texnoposedufinance.databinding.ActionBarAddBinding
import uz.texnopos.texnoposedufinance.databinding.FragmentCreateParticipantBinding
import uz.texnopos.texnoposedufinance.ui.main.group.add.CalendarDialog
import uz.texnopos.texnoposedufinance.ui.main.student.StudentViewModel
import java.util.*

class CreateParticipantFragment : BaseFragment(R.layout.fragment_create_participant) {
    lateinit var binding: FragmentCreateParticipantBinding
    lateinit var actBinding: ActionBarAddBinding
    private lateinit var navController: NavController
    private lateinit var parentNavController: NavController
    private val args: CreateParticipantFragmentArgs by navArgs()
    private val viewModel: StudentViewModel by viewModel()
    private lateinit var myGroup: Group
    private lateinit var studentId: String
    private var birthDate: Long = 0L
    private lateinit var address: String
    private var contractNum = 0
    private val auth: FirebaseAuth by inject()
    private lateinit var name: String
    private lateinit var passport: String
    private lateinit var phone: ArrayList<String>
    private var createdDate: Long = 0L
    private lateinit var participant: CreateParticipantRequest
    private lateinit var orgId: String

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCreateParticipantBinding.bind(view)
        actBinding = ActionBarAddBinding.bind(view)
        navController = Navigation.findNavController(view)
        orgId = auth.currentUser!!.uid

        val gson = Gson()
        myGroup = gson.fromJson(args.group, Group::class.java)

        actBinding.apply {
            btnHome.onClick {
                navController.popBackStack()
            }
            actionBarTitle.text = view.context.getString(R.string.addStudent)
        }

        binding.apply {
            etBirthDate.onClick {
                val dialog = CalendarDialog(requireContext())
                dialog.show()
                createdDate = Calendar.getInstance().timeInMillis
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
                        birthDate = cal.timeInMillis
                        etBirthDate.setText("$dStr.$mStr.$yStr")
                        dialog.dismiss()
                    }
                    btnCancel.onClick {
                        dialog.dismiss()
                    }
                }
            }

            if (requireParentFragment().requireActivity() is MainActivity) {
                parentNavController = Navigation.findNavController(
                    requireParentFragment().requireActivity() as
                            MainActivity, R.id.nav_host
                )
            }
            btnSave.onClick {
                name = etName.text.toString()
                val phone1 = etPhone1.text.toString()
                val phone2 = etPhone2.text.toString()
                address = etAddress.text.toString()
                passport = etPassportNum.text.toString()
                val contract = etContractNum.text.toString()
                if (name.isEmpty()) etName.error = view.context.getString(R.string.fillField)
                if (phone1.isEmpty()) etPhone1.error = view.context.getString(R.string.fillField)
                if (phone2.isEmpty()) etPhone2.error = view.context.getString(R.string.fillField)
                if (contract.isNotEmpty()) contractNum = contract.toInt()
                if (passport.isEmpty()) etPassportNum.error =
                    view.context.getString(R.string.fillField)
                if (address.isEmpty()) etAddress.error = view.context.getString(R.string.fillField)
                if (birthDate == 0L) etBirthDate.error = view.context.getString(R.string.fillField)
                if (contract.isEmpty()) etContractNum.error =
                    view.context.getString(R.string.fillField)
                scrollView.fullScroll(ScrollView.FOCUS_DOWN)
                if (name.isNotEmpty() && phone1.isNotEmpty() && phone2.isNotEmpty() && passport.isNotEmpty()
                    && address.isNotEmpty() && birthDate != 0L && contract.isNotEmpty()
                ) {
                    phone = arrayListOf(phone1, phone2)
                    studentId = UUID.randomUUID().toString()
                    participant = CreateParticipantRequest(
                        auth.currentUser!!.uid,
                        myGroup.courseId,
                        myGroup.id,
                        studentId,
                        name,
                        passport,
                        contractNum,
                        birthDate,
                        createdDate,
                        phone,
                        address,
                        myGroup.courseName
                    )
                    viewModel.createParticipantIfStudentNotExists(participant)
                }
            }
        }
        setUpObservers()
    }

    private fun setUpObservers() {
        binding.apply {
            viewModel.createParticipant.observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    ResourceState.LOADING -> {
                        isLoading(true)
                    }
                    ResourceState.SUCCESS -> {
                        isLoading(false)
                        if (it.data == "exists") {
                            val dialog = AlertDialog.Builder(requireContext())
                            dialog.setTitle(context?.getString(R.string.thisStudentWasPreviouslyAdded))
                            dialog.setMessage(context?.getString(R.string.createStudentDialog))
                            dialog.setNegativeButton(context?.getString(R.string.select)) { d, _ ->
                                val gsonPretty = GsonBuilder().setPrettyPrinting().create()
                                val jsonString = gsonPretty.toJson(
                                    SendParticipantDataRequest(
                                        id = studentId,
                                        studentId = "",
                                        groupId = myGroup.id,
                                        courseId = myGroup.courseId,
                                        orgId = auth.currentUser!!.uid,
                                        passport = passport,
                                        contract = contractNum,
                                        phone = phone
                                    )
                                )
                                val action =
                                    CreateParticipantFragmentDirections.actionCreateParticipantFragmentToSelectExistingStudentFragment(
                                        jsonString
                                    )
                                parentNavController.navigate(action)
                                d.dismiss()
                            }
                            dialog.setPositiveButton(context?.getString(R.string.add)) { d, _ ->
                                viewModel.createParticipantWithNewStudent(
                                    CreateParticipantRequest(
                                        auth.currentUser!!.uid,
                                        myGroup.courseId,
                                        myGroup.id,
                                        studentId,
                                        name,
                                        passport,
                                        contractNum,
                                        birthDate,
                                        createdDate,
                                        phone,
                                        address,
                                        myGroup.courseName
                                    )
                                )
                                d.dismiss()
                                navController.popBackStack()
                            }
                            dialog.show()
                        }
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
                        isLoading(false)
                    }
                }
            })

            viewModel.student.observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    ResourceState.LOADING -> {
                        isLoading(true)
                    }
                    ResourceState.SUCCESS -> {
                        isLoading(false)
                        if (it.data == "contract exists") {
                            toastLN(context?.getString(R.string.contractExists))
                        }
                        if (it.data == "success") {
                            toastLN(context?.getString(R.string.added_successfully))
                            navController.popBackStack()
                        }
                    }
                    ResourceState.ERROR -> {
                        isLoading(false)
                        toastLN(it.message)
                    }
                }
            })
        }
    }

    private fun isLoading(b: Boolean) {
        binding.apply {
            etName.isEnabled = !b
            etPhone1.isEnabled = !b
            etPhone2.isEnabled = !b
            etAddress.isEnabled = !b
            etPassportNum.isEnabled = !b
            etContractNum.isEnabled = !b
            btnSave.isEnabled = !b
            etBirthDate.isEnabled = !b
            loading.visibility(b)
        }
    }
}
