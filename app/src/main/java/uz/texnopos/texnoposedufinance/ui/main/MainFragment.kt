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
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment
import uz.texnopos.texnoposedufinance.core.extentions.onClick
import uz.texnopos.texnoposedufinance.databinding.BottomSheetAddBinding
import uz.texnopos.texnoposedufinance.databinding.BottomSheetAddStudentBinding
import uz.texnopos.texnoposedufinance.databinding.FragmentMainBinding

class MainFragment: BaseFragment(R.layout.fragment_main) {
    private lateinit var binding: FragmentMainBinding
    lateinit var navController: NavController
    lateinit var childNavController: NavController
    lateinit var groupId: String
    lateinit var courseId: String
    lateinit var bottomSheetAddStudentBinding: BottomSheetAddStudentBinding

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
                R.id.nav_student ->{
                    val action = MainFragmentDirections.actionMainFragmentToAddStudentFragment()
                    navController.navigate(action)
                }
                R.id.nav_course -> {
                    val action = MainFragmentDirections.actionMainFragmentToAddCoursesFragment()
                    navController.navigate(action)
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
                            if (::groupId.isInitialized && ::courseId.isInitialized) {
                                val action = MainFragmentDirections.actionMainFragmentToAddNewStudentParticipantFragment(groupId, courseId)
                                navController.navigate(action)

                            }
                        }
                        selectStudents.onClick {
                            bottomSheetDialog.dismiss()
                            val action = MainFragmentDirections.actionMainFragmentToSelectStudentsFragment()
                            navController.navigate(action)
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