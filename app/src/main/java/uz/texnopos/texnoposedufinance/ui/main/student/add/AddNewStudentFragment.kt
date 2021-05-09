package uz.texnopos.texnoposedufinance.ui.main.student.add

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


class AddStudentFragment: BaseFragment(R.layout.fragment_add_student) {
    lateinit var binding: FragmentAddStudentBinding
    lateinit var actBinding: ActionBarAddBinding
    private lateinit var navController: NavController
    private val viewModel: AddStudentViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddStudentBinding.bind(view)
        actBinding =  ActionBarAddBinding.bind(view)
        navController = Navigation.findNavController(view)
        setUpObservers()
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
                val interested = etStudy.text.toString()
                if(name.isEmpty()) etName.error = view.context.getString(R.string.fillField)
                if(phone1.isEmpty()) etPhone1.error = view.context.getString(R.string.fillField)
                if(phone2.isEmpty()) etPhone2.error = view.context.getString(R.string.fillField)
                if(interested.isEmpty()) etStudy.error = view.context.getString(R.string.fillField)

                if(name.isNotEmpty() && phone1.isNotEmpty() && interested.isNotEmpty() && phone2.isNotEmpty()){
                    val phone = arrayListOf(phone1, phone2)
                    viewModel.addStudent("", "", name, phone, interested, "", "", "", 0)
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
            etStudy.isEnabled = !b
            btnSave.isEnabled = !b
            loading.visibility(b)
        }
    }
}