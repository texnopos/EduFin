package uz.texnopos.texnoposedufinance.ui.main.student.add

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import org.koin.android.viewmodel.ext.android.viewModel
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment
import uz.texnopos.texnoposedufinance.core.ResourceState
import uz.texnopos.texnoposedufinance.core.extentions.onClick
import uz.texnopos.texnoposedufinance.core.extentions.visibility
import uz.texnopos.texnoposedufinance.databinding.ActionBarAddBinding
import uz.texnopos.texnoposedufinance.databinding.FragmentAddStudentBinding
import uz.texnopos.texnoposedufinance.ui.main.group.add.CalendarDialog
import uz.texnopos.texnoposedufinance.ui.main.student.StudentViewModel
import java.util.*

class CreateStudentFragment : BaseFragment(R.layout.fragment_add_student) {
    lateinit var binding: FragmentAddStudentBinding
    lateinit var actBinding: ActionBarAddBinding
    private lateinit var navController: NavController
    private val viewModel: StudentViewModel by viewModel()
    lateinit var studentId: String
    private var createdDate = 0L
    private var birthDate = 0L
    var passportList = arrayOf<String>()
    private val args: CreateStudentFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddStudentBinding.bind(view)
        actBinding = ActionBarAddBinding.bind(view)
        navController = Navigation.findNavController(view)
        setUpObserversStudent()
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
            btnSave.onClick {
                var passport = etPassportNum.text.toString()
                passport = passport.replace("\\s".toRegex(), "")
                val name = etName.text.toString()
                var phone1 = etPhone1.text.toString()
                phone1 = phone1.replace("\\s".toRegex(), "")
                var phone2 = etPhone2.text.toString()
                phone2 = phone2.replace("\\s".toRegex(), "")
                val interested = etStudy.text.toString()
                val address = etAddress.text.toString()
                if (passport.isEmpty()) etPassportNum.error =
                    view.context.getString(R.string.fillField)
                if (name.isEmpty()) etName.error = view.context.getString(R.string.fillField)
                if (phone1.isEmpty()) etPhone1.error = view.context.getString(R.string.fillField)
                if (phone2.isEmpty()) etPhone2.error = view.context.getString(R.string.fillField)
                if (interested.isEmpty()) etStudy.error = view.context.getString(R.string.fillField)
                if (address.isEmpty()) etAddress.error = view.context.getString(R.string.fillField)
                if (name.isNotEmpty() && phone1.isNotEmpty() && interested.isNotEmpty() && phone2.isNotEmpty()) {
                    val phone = arrayListOf(phone1, phone2)
                    studentId = UUID.randomUUID().toString()
                    passportList = args.passportList
                    var k = true
                    for (element in passportList) {
                        if (passport == element) {
                            k = false
                            break
                        }
                    }
                    if (!k) {
                        val dialog = AlertDialog.Builder(requireContext())
                        dialog.apply {
                            setTitle(context?.getString(R.string.attention))
                            setMessage(context?.getString(R.string.thisStudentWasPreviouslyAdded))
                            setPositiveButton(R.string.doItAnyway) { d, _ ->
                                viewModel.addStudent(
                                    studentId,
                                    name,
                                    phone,
                                    interested,
                                    passport,
                                    birthDate,
                                    createdDate,
                                    address
                                )
                                isLoading(true)
                                d.dismiss()
                            }
                            setNegativeButton(R.string.cancel) { d, _ ->
                                d.dismiss()
                            }
                            show()
                        }

                    } else {
                        viewModel.addStudent(
                            studentId,
                            name,
                            phone,
                            interested,
                            passport,
                            birthDate,
                            createdDate,
                            address
                        )
                        isLoading(true)
                    }

                }
            }
        }
    }

    private fun setUpObserversStudent() {
        binding.apply {
            viewModel.createStudent.observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    ResourceState.LOADING -> {
                        isLoading(true)
                    }
                    ResourceState.SUCCESS -> {
                        isLoading(false)
                        navController.popBackStack()
                        toastLN(view?.context!!.getString(R.string.added_successfully))
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
            etStudy.isEnabled = !b
            btnSave.isEnabled = !b
            loading.visibility(b)
        }
    }
}