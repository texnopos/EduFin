package uz.texnopos.texnoposedufinance.ui.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.functions.FirebaseFunctions
import org.koin.android.ext.android.inject
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment

class SplashFragment : BaseFragment(R.layout.fragment_splash) {
    private val auth: FirebaseAuth by inject()
    private val functions: FirebaseFunctions by inject()
    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        Handler(Looper.getMainLooper()).postDelayed({
            if (auth.currentUser == null) {
                val action = SplashFragmentDirections.actionSplashFragmentToSignInFragment()
                navController.navigate(action)
            } else {
                val action = SplashFragmentDirections.actionSplashFragmentToMainFragment()
                navController.navigate(action)
            }
        }, 3000)

    }
}