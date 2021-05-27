package uz.texnopos.texnoposedufinance.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment
import uz.texnopos.texnoposedufinance.core.ResourceState
import uz.texnopos.texnoposedufinance.core.extentions.onClick
import uz.texnopos.texnoposedufinance.databinding.BottomSheetAddStudentBinding
import uz.texnopos.texnoposedufinance.databinding.FragmentMainBinding
import kotlin.collections.ArrayList

class MainFragment : BaseFragment(R.layout.fragment_main) {
    private lateinit var binding: FragmentMainBinding
    lateinit var navController: NavController
    lateinit var childNavController: NavController
    lateinit var group: String
    lateinit var groupId: String
    var passportList: ArrayList<String> = arrayListOf()
    var pos = -1
    private lateinit var bottomSheetAddStudentBinding: BottomSheetAddStudentBinding

    @SuppressLint("InflateParams")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)
        setUpBottomNavigation()
        setAppBarCorersRadius()
        navController = Navigation.findNavController(view)
        childNavController = Navigation.findNavController(view.findViewById(R.id.main_nav_host))
        binding.bnvMain.background = null
        binding.bnvMain.menu.getItem(2).isEnabled = false
        binding.fabMain.onClick {
            when (childNavController.currentDestination?.id) {
                R.id.nav_teacher -> {
                    val action = MainFragmentDirections.actionMainFragmentToAddTeacherFragment()
                    navController.navigate(action)
                }
                R.id.nav_info -> {
                    val action = MainFragmentDirections.actionMainFragmentToAddInfoFragment()
                    navController.navigate(action)
                }
                R.id.nav_student -> {
                    if (passportList.size != 0) {
                        val action = MainFragmentDirections.actionMainFragmentToAddStudentFragment((passportList as List<String>).toTypedArray())
                        navController.navigate(action)
                    } else {
                        val action = MainFragmentDirections.actionMainFragmentToAddStudentFragment(arrayOf())
                        navController.navigate(action)
                    }
                }
                R.id.nav_course -> {
                    val action = MainFragmentDirections.actionMainFragmentToAddCoursesFragment()
                    navController.navigate(action)
                }
                R.id.nav_category -> {
                    if (pos != -1){
                        val action = MainFragmentDirections.actionMainFragmentToAddCategoryFragment(pos)
                        navController.navigate(action)
                    }
                }
                R.id.nav_group_info -> {
                    val bottomSheetDialog = BottomSheetDialog(requireContext())
                    val v = layoutInflater.inflate(R.layout.bottom_sheet_add_student, null)
                    bottomSheetAddStudentBinding = BottomSheetAddStudentBinding.bind(v)
                    bottomSheetDialog.setContentView(v)
                    bottomSheetDialog.show()

                    bottomSheetAddStudentBinding.apply {
                        addNewStudent.onClick {
                            bottomSheetDialog.dismiss()
                            if (::group.isInitialized) {
                                val action = MainFragmentDirections.actionMainFragmentToAddParticipantFragment(group)
                                navController.navigate(action)
                            }
                        }
                        selectStudents.onClick {
                            bottomSheetDialog.dismiss()
                            if (::group.isInitialized) {
                                val action = MainFragmentDirections.actionMainFragmentToSelectStudentsFragment(group)
                                navController.navigate(action)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setUpBottomNavigation() {
        val navController = Navigation.findNavController(requireActivity(), R.id.main_nav_host)
        NavigationUI.setupWithNavController(binding.bnvMain, navController)
    }

    private fun setAppBarCorersRadius() {
        val radius = resources.getDimension(R.dimen.default_app_bar_radius)
        val bottomBarBackground = binding.babMain.background as MaterialShapeDrawable
        bottomBarBackground.shapeAppearanceModel =
            bottomBarBackground.shapeAppearanceModel
                .toBuilder()
                .setTopRightCorner(CornerFamily.ROUNDED, radius)
                .setTopLeftCorner(CornerFamily.ROUNDED, radius)
                .build()
    }
}