package uz.texnopos.texnoposedufinance.ui.main.student.add

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import org.koin.android.viewmodel.ext.android.viewModel
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment
import uz.texnopos.texnoposedufinance.core.ResourceState
import uz.texnopos.texnoposedufinance.core.extentions.onClick
import uz.texnopos.texnoposedufinance.databinding.ActionBarAddBinding
import uz.texnopos.texnoposedufinance.databinding.FragmentAddStudentBinding

class AddStudentFragment: BaseFragment(R.layout.fragment_add_student) {
    lateinit var binding: FragmentAddStudentBinding
    lateinit var actBinding: ActionBarAddBinding
    private lateinit var navController: NavController
    private val viewModel: AddStudentViewModel by viewModel()
    private val args: AddStudentFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddStudentBinding.bind(view)
        actBinding =  ActionBarAddBinding.bind(view)
        navController = Navigation.findNavController(view)
        toastLN(args.groupId)
        setUpObservers()
        actBinding.apply {
            btnHome.onClick{
                navController.popBackStack()
            }
        }
        binding.apply {
            btnSave.onClick {
                viewModel.addStudent(etPhone.text.toString(), args.groupId, etName.text.toString())
            }
        }
    }
    private fun setUpObservers(){
        binding.apply {
            viewModel.student.observe(viewLifecycleOwner, Observer {
                when(it.status){
                    ResourceState.LOADING ->{

                    }
                    ResourceState.SUCCESS ->{
                        toastLN("added successfully")
                    }
                    ResourceState.ERROR ->{
                        toastLN(it.message)
                    }
                }
            })
        }
    }
}