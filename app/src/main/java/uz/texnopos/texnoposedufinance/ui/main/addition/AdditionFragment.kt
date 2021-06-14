package uz.texnopos.texnoposedufinance.ui.main.addition

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.viewbinding.ViewBinding
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.ext.android.bind
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import uz.texnopos.texnoposedufinance.MainActivity
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment
import uz.texnopos.texnoposedufinance.core.extentions.onClick
import uz.texnopos.texnoposedufinance.databinding.BottomSheetAddBinding
import uz.texnopos.texnoposedufinance.databinding.DialogCalendarBinding
import uz.texnopos.texnoposedufinance.databinding.FragmentAdditionBinding
import uz.texnopos.texnoposedufinance.ui.auth.signin.SignInViewModel
import uz.texnopos.texnoposedufinance.ui.main.MainFragmentDirections

class AdditionFragment: BottomSheetDialogFragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_add, container, false)
    }

    private lateinit var binding: BottomSheetAddBinding
    private lateinit var navController: NavController
    private lateinit var parentNavController: NavController
    private val viewModel: SignInViewModel by viewModel()
    private val mGoogleSignInClient: GoogleSignInClient by inject()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = BottomSheetAddBinding.bind(view)
        navController = findNavController(requireActivity(), R.id.main_nav_host)
        if (requireParentFragment().requireActivity() is MainActivity) {
            parentNavController = Navigation.findNavController(
                requireParentFragment().requireActivity() as MainActivity,
                R.id.nav_host
            )
        }
        binding.apply {
            teacher.onClick {
                dismiss()
                val action = AdditionFragmentDirections.actionNavAdditionToTeacherFragment()
                navController.navigate(action)
            }
            student.onClick {
                dismiss()
                val action = AdditionFragmentDirections.actionNavAdditionToStudentsFragment()
                navController.navigate(action)
            }
            info.onClick {
                dismiss()
                val action = AdditionFragmentDirections.actionNavAdditionToInfoFragment()
                navController.navigate(action)
            }
            category.onClick {
                dismiss()
                val action = AdditionFragmentDirections.actionNavAdditionToCategoryFragment()
                navController.navigate(action)
            }
            signOut.onClick {
                val dialog = AlertDialog.Builder(requireContext())
                dialog.setTitle(context?.getString(R.string.sign_out))
                dialog.setMessage(context?.getString(R.string.sign_out_text))
                dialog.setIcon(R.drawable.ic_warning)
                dialog.setPositiveButton(context?.getString(R.string.yes)) { d, _ ->
                    d.dismiss()
                    mGoogleSignInClient.signOut()
                    viewModel.signOut()
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