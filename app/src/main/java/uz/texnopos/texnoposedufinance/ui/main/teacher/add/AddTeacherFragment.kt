package uz.texnopos.texnoposedufinance.ui.main.teacher.add

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
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
import uz.texnopos.texnoposedufinance.databinding.FragmentAddTeacherBinding

class AddTeacherFragment : BaseFragment(R.layout.fragment_add_teacher) {

    private val viewModel: AddTeacherViewModel by viewModel()
    private lateinit var binding: FragmentAddTeacherBinding
    private lateinit var navController: NavController
    private lateinit var bindingActionBar: AddActionBarBinding
    var showPassword = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        binding = FragmentAddTeacherBinding.bind(view)
        bindingActionBar = AddActionBarBinding.bind(view)
        
        bindingActionBar.actionBarTitle.text = view.context.getString(R.string.addTeacher)
        setUpObservers()

        bindingActionBar.btnHome.onClick {
            navController.popBackStack()
        }
        binding.apply {

           showPass.onClick {
               showPassword()
           }

            btnSave.onClick {
                val name = etName.text.toString()
                val phone = etPhone.text.toString()
                val username = etUsername.text.toString()
                val password = etPassword.text.toString()
                val confirmPass = etConfirmPass.text.toString()
                val salary = etSalary.text.toString()

                if (name.isNotEmpty() && phone.isNotEmpty() &&
                    username.isNotEmpty() && password.isNotEmpty() &&
                    salary.isNotEmpty() && confirmPass.isNotEmpty()) {
                    if(password.length > 7){
                        if(password == confirmPass)
                            viewModel.createTeacher(name, phone, username, password, salary.toDouble())
                        else {
                            etConfirmPass.text!!.clear()
                            etConfirmPass.error = view.context.getString(R.string.doNotMatch)
                        }
                    }
                    else {
                        etPassword.error = view.context.getString(R.string.minLength)
                    }

                } else {
                    if (name.isEmpty()) etName.error = view.context.getString(R.string.fillField)
                    if (phone.isEmpty()) etPhone.error = view.context.getString(R.string.fillField)
                    if (salary.isEmpty()) etSalary.error = view.context.getString(R.string.fillField)
                    if (username.isEmpty()) etUsername.error = view.context.getString(R.string.fillField)
                    if (password.isEmpty()) etPassword.error = view.context.getString(R.string.fillField)
                    if (confirmPass.isEmpty()) etConfirmPass.error = view.context.getString(R.string.fillField)
                }
            }
        }
    }

    private fun setUpObservers() {
        binding.apply {
            viewModel.createTeacher.observe(
                viewLifecycleOwner
            ) {
                when (it.status) {
                    ResourceState.LOADING -> {
                        loading.visibility = View.VISIBLE
                        isLoading(true)
                    }
                    ResourceState.SUCCESS -> {
                        loading.visibility = View.GONE
                        isLoading(false)
                        empty()
                        toastLN("Добавлен новый сотрудник")
                    }
                    ResourceState.ERROR -> {
                        isLoading(false)
                        toastLN(it.message)
                    }
                }
            }
        }
    }

    private fun isLoading(b: Boolean) {
        binding.apply {
            etUsername.isEnabled = !b
            etPhone.isEnabled = !b
            etName.isEnabled = !b
            etPassword.isEnabled = !b
            etSalary.isEnabled = !b
            etConfirmPass.isEnabled = !b
            btnSave.isEnabled = !b
        }
    }
    private fun empty() {
        binding.apply {
            etUsername.text!!.clear()
            etPhone.text!!.clear()
            etSalary.text!!.clear()
            etPassword.text!!.clear()
            etName.text!!.clear()
            etConfirmPass.text!!.clear()
            showPassword = false
        }
    }
    private fun showPassword(){
        if(showPassword){
            binding.etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            binding.showPass.setImageResource(R.drawable.ic_visible)
            showPassword = false
        }
        else {
            binding.etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            binding.showPass.setImageResource(R.drawable.ic_unvisible)
            showPassword = true
        }
    }
}
