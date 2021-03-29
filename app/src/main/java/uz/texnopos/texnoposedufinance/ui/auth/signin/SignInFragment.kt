package uz.texnopos.texnoposedufinance.ui.auth.signin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
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


        binding.btnSignIn.onClick {
            val email: String = binding.etEmail.text.toString()
            val password: String = binding.etPassword.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()){
                viewModel.signIn(email, password)
            }
            else {
                if (email.isEmpty())
                    binding.etEmail.error = getString(R.string.email_not_entered_error)
                if (password.isEmpty())
                    binding.etPassword.error = getString(R.string.enter_your_password_error)
            }
        }
        binding.btnSignUp.onClick {
            val action = SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
            navController.navigate(action)
        }
        binding.btnGoogle.onClick {
            signIn()
        }
    }

    private fun setUpObservers() {
        viewModel.signInResult.observe(viewLifecycleOwner, Observer{
            when(it.status) {
                ResourceState.LOADING -> binding.loading.visibility(true)
                ResourceState.SUCCESS -> {
                    binding.loading.visibility(false)
                    val action = SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
                    navController.navigate(action)
                }
                ResourceState.ERROR -> {
                    toastLN(it.message)
                    binding.loading.visibility(false)
                }
            }
        })
    }

    private fun setUpObserversGoogle() {
        viewModel.googleAuthResult.observe(viewLifecycleOwner, Observer{
            when(it.status) {
                ResourceState.SUCCESS -> {
                    binding.loading.visibility(false)
                    val action = SignInFragmentDirections.actionSignInFragmentToMainFragment()
                    navController.navigate(action)
                }
                ResourceState.ERROR -> {
                    toastLN(it.message)
                    binding.loading.visibility(false)
                }
                else -> {
                    binding.loading.visibility(true)
                }
            }

        })
    }

    private fun signIn() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, SIGN_IN)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.let {
                    viewModel.firebaseAuthWithGoogle(it)
                    setUpObserversGoogle()
                }
            } catch (e: ApiException) {
                when(e.statusCode){
                    7-> toastLN("Internetti qosin")
                }
                Toast.makeText(requireContext(), e.localizedMessage/*"Google sign in failed:("*/, Toast.LENGTH_LONG).show()
            }
        }
    }

}