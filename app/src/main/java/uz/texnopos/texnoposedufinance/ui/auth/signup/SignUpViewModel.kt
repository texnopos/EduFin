package uz.texnopos.texnoposedufinance.ui.auth.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.texnopos.texnoposedufinance.core.Resource
import uz.texnopos.texnoposedufinance.data.firebase.AuthHelper

class SignUpViewModel(private val authHelper: AuthHelper) : ViewModel() {
    private var _signUpResult: MutableLiveData<Resource<String>> = MutableLiveData()
    val signUpResult: LiveData<Resource<String>>
        get() = _signUpResult

    fun signUp(email: String, password: String) {
        _signUpResult.value = Resource.loading()
        authHelper.signUp(email, password, {
            _signUpResult.value = Resource.success("Success")
        }, {
            _signUpResult.value = Resource.error(it)
        })
    }
}