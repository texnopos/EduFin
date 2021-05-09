package uz.texnopos.texnoposedufinance.ui.main.addition

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.bottomsheet.BottomSheetDialog
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment
import uz.texnopos.texnoposedufinance.core.extentions.onClick
import uz.texnopos.texnoposedufinance.databinding.BottomSheetAddBinding
import uz.texnopos.texnoposedufinance.databinding.FragmentAdditionBinding

class AdditionFragment: BaseFragment(R.layout.fragment_addition) {
    lateinit var binding: FragmentAdditionBinding
    lateinit var bottomSheetAddBinding: BottomSheetAddBinding
    lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAdditionBinding.bind(view)
        navController = Navigation.findNavController(view)
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val v = layoutInflater.inflate(R.layout.bottom_sheet_add, null)
        bottomSheetAddBinding = BottomSheetAddBinding.bind(v)
        bottomSheetDialog.setContentView(v)
        bottomSheetDialog.show()

        bottomSheetAddBinding.apply {
            teacher.onClick {
                bottomSheetDialog.dismiss()
                val action = AdditionFragmentDirections.actionNavAdditionToTeacherFragment()
                navController.navigate(action)
            }

            student.onClick {
                bottomSheetDialog.dismiss()
                val action = AdditionFragmentDirections.actionNavAdditionToNavStudent()
                navController.navigate(action)
            }
        }
    }
}