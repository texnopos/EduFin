package uz.texnopos.texnoposedufinance.ui.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.functions.FirebaseFunctions
import org.koin.android.ext.android.inject
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment
import uz.texnopos.texnoposedufinance.databinding.FragmentSplashBinding
import uz.texnopos.texnoposedufinance.ui.app.MainActivity

class SplashFragment : BaseFragment(R.layout.fragment_splash) {
    private val auth: FirebaseAuth by inject()
    private lateinit var navController: NavController
    private lateinit var binding: FragmentSplashBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        binding = FragmentSplashBinding.bind(view)
        binding.apply {
            texnoposLogo.setMinAndMaxFrame(0, 357)
            texnoposLogo.playAnimation()
        }
        Handler(Looper.getMainLooper()).postDelayed({
            if (auth.currentUser == null) {
                val action = SplashFragmentDirections.actionSplashFragmentToSignInFragment()
                navController.navigate(action)
            } else {
                val action = SplashFragmentDirections.actionSplashFragmentToMainFragment()
                navController.navigate(action)
            }
        }, 4000)

    }
}