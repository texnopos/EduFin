package uz.texnopos.texnoposedufinance.ui.main.teacher.add

import android.os.Bundle
import android.view.View
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.Navigation
import org.koin.android.viewmodel.ext.android.viewModel
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment
import uz.texnopos.texnoposedufinance.core.ResourceState
import uz.texnopos.texnoposedufinance.core.extentions.onClick
import uz.texnopos.texnoposedufinance.databinding.AddActionBarBinding
import uz.texnopos.texnoposedufinance.databinding.FragmentAddEmployeeBinding

class AddTeacherFragment : BaseFragment(R.layout.fragment_add_employee) {

    private val viewModel: AddTeacherViewModel by viewModel()
    private lateinit var binding: FragmentAddEmployeeBinding
    private lateinit var navController: NavController
    private lateinit var bindingActionBar: AddActionBarBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        binding = FragmentAddEmployeeBinding.bind(view)
        bindingActionBar = AddActionBarBinding.bind(view)
        
        bindingActionBar.actionBarTitle.text = "Добавить учителя"
        setUpObservers()

        bindingActionBar.btnHome.onClick {
            navController.popBackStack()
        }
        binding.btnSave.onClick {
            val name = binding.etName.text.toString()
            val phone = binding.etPhone.text.toString()
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()

            if (name.isNotEmpty() && phone.isNotEmpty() && username.isNotEmpty() && password.isNotEmpty()) {
                viewModel.createTeacher(name, phone, username, password)
            } else {
                if (name.isEmpty()) binding.etName.error = "error"
                if (phone.isEmpty()) binding.etPhone.error = "error"
                if (username.isEmpty()) binding.etUsername.error = "error"
                if (password.isEmpty()) binding.etPassword.error = "error"
            }
        }
    }

    private fun setUpObservers() {
        viewModel.createTeacher.observe(
            viewLifecycleOwner
        ) {
            when (it.status) {
                ResourceState.LOADING -> {
                    binding.loading.visibility = View.VISIBLE
                    isLoading(true)
                }
                ResourceState.SUCCESS -> {
                    binding.loading.visibility = View.GONE
                    isLoading(false)
                    empty()
                    toastLN("Added new teacher")
                }
                ResourceState.ERROR -> {
                    isLoading(false)
                    toastLN(it.message)
                }
            }
        }
    }

    private fun isLoading(b: Boolean) {
        binding.etUsername.isEnabled = !b
        binding.etPhone.isEnabled = !b
        binding.etName.isEnabled = !b
        binding.etPassword.isEnabled = !b
        binding.btnSave.isEnabled = !b
    }

    private fun empty() {
        binding.etName.text!!.clear()
        binding.etPhone.text!!.clear()
        binding.etUsername.text!!.clear()
        binding.etPassword.text!!.clear()
    }
}
