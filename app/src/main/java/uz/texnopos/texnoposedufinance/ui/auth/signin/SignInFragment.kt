package uz.texnopos.texnoposedufinance.ui.auth.signin

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment
import uz.texnopos.texnoposedufinance.core.ResourceState
import uz.texnopos.texnoposedufinance.core.extentions.onClick
import uz.texnopos.texnoposedufinance.core.extentions.visibility
import uz.texnopos.texnoposedufinance.databinding.FragmentSignInBinding

class SignInFragment : BaseFragment(R.layout.fragment_sign_in) {

    private lateinit var navController: NavController
    private val viewModel: SignInViewModel by viewModel()
    private lateinit var binding: FragmentSignInBinding

    companion object{
        private const val SIGN_IN: Int = 1
    }
    private val mGoogleSignInClient: GoogleSignInClient by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSignInBinding.bind(view)
        navController = Navigation.findNavController(view)
        setUpObservers()
        setUpObserversGoogle()

        binding.apply {
            btnSignIn.onClick {
                val email: String = etEmail.text.toString()
                val password: String = etPassword.text.toString()
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    viewModel.signIn(email, password)
                } else {
                    if (email.isEmpty())
                        etEmail.error = getString(R.string.email_not_entered_error)
                    if (password.isEmpty())
                        etPassword.error = getString(R.string.enter_your_password_error)
                }
            }
            btnSignUp.onClick {
                val action = SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
                navController.navigate(action)
            }
            btnGoogle.onClick {
                signIn()
            }
        }
    }

    private fun setUpObservers() {
        binding.apply {
            viewModel.signInResult.observe(viewLifecycleOwner, Observer{
                when(it.status) {
                    ResourceState.LOADING -> loading.visibility(true)
                    ResourceState.SUCCESS -> {
                        loading.visibility(false)
                        val action = SignInFragmentDirections.actionSignInFragmentToMainFragment()
                        navController.navigate(action)
                    }
                    ResourceState.ERROR -> {
                        toastLN(it.message)
                        loading.visibility(false)
                    }
                }
            })
        }
    }

    private fun setUpObserversGoogle() {
        binding.apply {
            viewModel.googleAuthResult.observe(viewLifecycleOwner, Observer{
                when(it.status) {
                    ResourceState.SUCCESS -> {
                        loading.visibility(false)
                        val action = SignInFragmentDirections.actionSignInFragmentToMainFragment()
                        navController.navigate(action)
                    }
                    ResourceState.ERROR -> {
                        toastLN(it.message)
                        loading.visibility(false)
                    }
                    else -> {
                        loading.visibility(true)
                    }
                }

            })
        }
    }
    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, SIGN_IN)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.let {
                    viewModel.firebaseAuthWithGoogle(it)
                }
            } catch (e: ApiException) {
                when(e.statusCode){
                    7-> toastLN(context?.getString(R.string.connectInternet))
                    else -> toastLN(e.localizedMessage)
                }
            }
        }
    }
}