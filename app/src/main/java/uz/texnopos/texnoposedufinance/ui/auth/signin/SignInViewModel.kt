package uz.texnopos.texnoposedufinance.ui.auth.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import uz.texnopos.texnoposedufinance.core.Resource
import uz.texnopos.texnoposedufinance.data.firebase.AuthHelper

class SignInViewModel(private val authHelper: AuthHelper) : ViewModel() {

    private var _signInResult: MutableLiveData<Resource<String>> = MutableLiveData()
    val signInResult: LiveData<Resource<String>>
        get() = _signInResult

    private val _googleAuthResult: MutableLiveData<Resource<String>> = MutableLiveData()
    val googleAuthResult: LiveData<Resource<String>>
        get() = _googleAuthResult

    fun signIn(email: String, password: String) {
        _signInResult.value = Resource.loading()
        authHelper.signIn(email, password,
            {
                _signInResult.value = Resource.success("success")
            },
            {
                _signInResult.value = Resource.error(it)
            })
    }

    fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        _googleAuthResult.value = Resource.loading()
        authHelper.firebaseAuthWithGoogle(account,
            {
                _googleAuthResult.value = Resource.success("success")
            },
            {
                _googleAuthResult.value = Resource.error(it)
            })
    }

    fun signOut(){
        authHelper.signOut()
    }
}