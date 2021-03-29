package uz.texnopos.texnoposedufinance.ui.auth.signup

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import org.koin.android.viewmodel.ext.android.viewModel
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment
import uz.texnopos.texnoposedufinance.core.ResourceState
import uz.texnopos.texnoposedufinance.core.extentions.onClick
import uz.texnopos.texnoposedufinance.databinding.FragmentSignUpBinding

class SignUpFragment : BaseFragment(R.layout.fragment_sign_up) {

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var navController: NavController
    private val viewModel: SignUpViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bind = FragmentSignUpBinding.bind(view)
        binding = bind

        navController = Navigation.findNavController(view)
        setUpObservers()
        binding.btnSignUp.onClick {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() &&
                confirmPassword.isNotEmpty() && password == confirmPassword
            ) {
                viewModel.signUp(email, password)
            } else {
                when {
                    email.isEmpty() -> binding.etEmail.error =
                        getString(R.string.enter_your_email_address)
                    password.isEmpty() -> binding.etPassword.error =
                        getString(R.string.enter_your_password)
                    confirmPassword.isEmpty() -> binding.etConfirmPassword.error =
                        getString(R.string.confirm_your_password)
                    password != confirmPassword -> binding.etConfirmPassword.error =
                        getString(R.string.confirm_password_error)
                }
            }
        }
    }

    private fun setUpObservers() {
        viewModel.signUpResult.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                ResourceState.LOADING -> binding.loading.visibility = View.VISIBLE
                ResourceState.SUCCESS -> {
                    toastLN("Sign in aynasina otemiz")
                    binding.loading.visibility = View.INVISIBLE
                    val action = SignUpFragmentDirections.actionSignUpFragmentToMyFragment()
                    navController.navigate(action)
                }
                ResourceState.ERROR -> {
                    toastLN(it.message)
                    binding.loading.visibility = View.INVISIBLE
                }
            }
        })
    }

}