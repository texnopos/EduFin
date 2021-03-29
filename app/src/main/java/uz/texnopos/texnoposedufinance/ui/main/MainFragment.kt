package uz.texnopos.texnoposedufinance.ui.main

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment
import uz.texnopos.texnoposedufinance.core.extentions.onClick
import uz.texnopos.texnoposedufinance.databinding.FragmentMainBinding


class MainFragment: BaseFragment(R.layout.fragment_main){
    private lateinit var binding: FragmentMainBinding

    lateinit var navController: NavController
    lateinit var childNavController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bind = FragmentMainBinding.bind(view)
        binding = bind

        setUpBottomNavigation()
        setAppBarCorersRadius()
        navController = Navigation.findNavController(view)
        childNavController = Navigation.findNavController(view.findViewById(R.id.main_nav_host))
        binding.bnvMain.background = null
        binding.bnvMain.menu.getItem(2).isEnabled = false
        binding.fabMain.onClick {
            when(childNavController.currentDestination?.id) {
                R.id.nav_employee -> {
                    val action = MainFragmentDirections.actionMainFragmentToAddTeacherFragment()
                    navController.navigate(action)
                }
                R.id.nav_course ->{
                    val action = MainFragmentDirections.actionMainFragmentToAddCoursesFragment()
                    navController.navigate(action)

                }
            }
        }
    }

    private fun setUpBottomNavigation(){
        val navController = Navigation.findNavController(requireActivity(), R.id.main_nav_host)
        NavigationUI.setupWithNavController(binding.bnvMain, navController)
    }

    private fun setAppBarCorersRadius() {
        val radius = resources.getDimension(R.dimen.default_app_bar_radius)
        val bottomBarBackground = binding.babMain.background as MaterialShapeDrawable
        bottomBarBackground.shapeAppearanceModel =
                bottomBarBackground.shapeAppearanceModel
                .toBuilder()
                .setTopRightCorner(CornerFamily.ROUNDED,radius)
                .setTopLeftCorner(CornerFamily.ROUNDED,radius)
                .build()
    }
}