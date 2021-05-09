package uz.texnopos.texnoposedufinance.ui.main.student.add

import android.os.Bundle
import android.view.View
import android.widget.ScrollView
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
import uz.texnopos.texnoposedufinance.databinding.FragmentAddParticipantBinding
import java.util.*

class AddNewStudentParticipantFragment: BaseFragment(R.layout.fragment_add_participant) {
    lateinit var binding: FragmentAddParticipantBinding
    lateinit var actBinding: ActionBarAddBinding
    private lateinit var navController: NavController
    private val args: AddNewStudentParticipantFragmentArgs by navArgs()
    private val viewModel: AddStudentViewModel by viewModel()
    lateinit var groupId: String
    lateinit var courseId: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddParticipantBinding.bind(view)
        actBinding =  ActionBarAddBinding.bind(view)
        navController = Navigation.findNavController(view)
        setUpObservers()
        groupId = args.groupId
        courseId = args.courseId
        actBinding.apply {
            btnHome.onClick{
                navController.popBackStack()
            }
            actionBarTitle.text = view.context.getString(R.string.addStudent)
        }
        binding.apply {
            btnSave.onClick {
                val name = etName.text.toString()
                val phone1 = etPhone1.text.toString()
                val phone2 = etPhone2.text.toString()
                val address = etAddress.text.toString()
                val passport = etPassportNum.text.toString()
                val birthDate = etBirthDate.text.toString()
                val contract = etContractNum.text.toString()
                var contractNum = 0
                if(name.isEmpty()) etName.error = view.context.getString(R.string.fillField)
                if(phone1.isEmpty()) etPhone1.error = view.context.getString(R.string.fillField)
                if(phone2.isEmpty()) etPhone2.error = view.context.getString(R.string.fillField)
                if(contract.isNotEmpty()) contractNum = contract.toInt()
                /*if(address.isEmpty()) etAddress.error = view.context.getString(R.string.fillField)
                if(passport.isEmpty()) etPassportNum.error = view.context.getString(R.string.fillField)
                if(birthDate.isEmpty()) etBirthDate.error = view.context.getString(R.string.fillField)
                if(contract.isEmpty()) etContractNum.error = view.context.getString(R.string.fillField)*/
                scrollView.fullScroll(ScrollView.FOCUS_DOWN)
                if(name.isNotEmpty() && phone1.isNotEmpty() && phone2.isNotEmpty() /*&& address.isNotEmpty() &&
                        passport.isNotEmpty() && birthDate.isNotEmpty() && contract.isNotEmpty()*/){
                    val phone = arrayListOf(phone1, phone2)
                    viewModel.addStudent(groupId, courseId, name, phone, "", passport, birthDate, address, contractNum)
                    isLoading(true)
                }
            }
        }
    }
    private fun setUpObservers(){
        binding.apply {
            viewModel.student.observe(viewLifecycleOwner, Observer {
                when(it.status){
                    ResourceState.LOADING ->{
                        isLoading(true)
                    }
                    ResourceState.SUCCESS ->{
                        isLoading(false)
                        navController.popBackStack()
                        toastLN(view?.context!!.getString(R.string.added_successfully))
                    }
                    ResourceState.ERROR ->{
                        isLoading(false)
                        toastLN(it.message)
                    }
                }
            })
        }
    }
    private fun isLoading(b: Boolean){
        binding.apply {
            etName.isEnabled = !b
            etPhone1.isEnabled = !b
            etPhone2.isEnabled = !b
            etAddress.isEnabled = !b
            etPassportNum.isEnabled = !b
            etBirthDate.isEnabled = !b
            etContractNum.isEnabled = !b
            btnSave.isEnabled = !b
            loading.visibility(b)
        }
    }
}