package uz.texnopos.texnoposedufinance.ui.internet

import android.app.Service
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment
import uz.texnopos.texnoposedufinance.databinding.FragmentInternetBinding

class InternetConnection: BaseFragment(R.layout.fragment_internet) {
    private lateinit var binding: FragmentInternetBinding

    private var connect: ConnectivityManager? = null
    var info: NetworkInfo? = null
    private lateinit var navController: NavController
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentInternetBinding.bind(view)
        navController = Navigation.findNavController(view)
        binding.apply {
            connect = requireContext().getSystemService(Service.CONNECTIVITY_SERVICE) as ConnectivityManager
            if(connect != null){
                info = connect!!.activeNetworkInfo
                if(info != null){
                    if(info!!.state == NetworkInfo.State.CONNECTED){
                        val action = InternetConnectionDirections.actionInternetConnectionToSplashFragment()
                        navController.navigate(action)
                    }
                }
                else{
                    toastLN("000")
                }
            }
        }
    }
}