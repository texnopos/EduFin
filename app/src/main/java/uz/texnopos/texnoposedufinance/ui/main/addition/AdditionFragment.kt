package uz.texnopos.texnoposedufinance.ui.main.addition

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject
import uz.texnopos.texnoposedufinance.MainActivity
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment
import uz.texnopos.texnoposedufinance.core.extentions.onClick
import uz.texnopos.texnoposedufinance.databinding.BottomSheetAddBinding
import uz.texnopos.texnoposedufinance.databinding.FragmentAdditionBinding
import uz.texnopos.texnoposedufinance.ui.main.MainFragmentDirections

class AdditionFragment : BaseFragment(R.layout.fragment_addition) {
    lateinit var binding: FragmentAdditionBinding
    lateinit var bottomSheetAddBinding: BottomSheetAddBinding
    lateinit var navController: NavController
    lateinit var parentNavController: NavController
    val auth: FirebaseAuth by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAdditionBinding.bind(view)
        navController = Navigation.findNavController(view)
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val v = layoutInflater.inflate(R.layout.bottom_sheet_add, null)
        bottomSheetAddBinding = BottomSheetAddBinding.bind(v)
        bottomSheetDialog.setContentView(v)
        bottomSheetDialog.show()
        if (requireParentFragment().requireActivity() is MainActivity) {
            parentNavController = Navigation.findNavController(
                requireParentFragment().requireActivity() as MainActivity,
                R.id.nav_host
            )
        }

        bottomSheetAddBinding.apply {
            teacher.onClick {
                bottomSheetDialog.dismiss()
                val action = AdditionFragmentDirections.actionNavAdditionToTeacherFragment()
                navController.navigate(action)
            }
            student.onClick {
                bottomSheetDialog.dismiss()
                val action = AdditionFragmentDirections.actionNavAdditionToStudentsFragment()
                navController.navigate(action)
            }
            signOut.onClick {
                val dialog = AlertDialog.Builder(requireContext())
                dialog.setTitle(context?.getString(R.string.sign_out))
                dialog.setMessage(context?.getString(R.string.sign_out_text))
                dialog.setIcon(R.drawable.ic_warning)
                dialog.setPositiveButton(context?.getString(R.string.yes)) { d, _ ->
                    d.dismiss()
                    auth.signOut()
                    bottomSheetDialog.dismiss()
                    val action = MainFragmentDirections.actionMainFragmentToSignInFragment()
                    parentNavController.navigate(action)
                }
                dialog.setNegativeButton(context?.getString(R.string.cancel)) { d, _ ->
                    d.dismiss()
                }
                dialog.show()
            }
        }
    }
}