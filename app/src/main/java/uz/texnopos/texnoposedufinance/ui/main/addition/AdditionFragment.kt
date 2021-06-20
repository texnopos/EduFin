package uz.texnopos.texnoposedufinance.ui.main.addition

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import uz.texnopos.texnoposedufinance.MainActivity
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment
import uz.texnopos.texnoposedufinance.core.extentions.onClick
import uz.texnopos.texnoposedufinance.databinding.*
import uz.texnopos.texnoposedufinance.ui.auth.signin.SignInViewModel
import uz.texnopos.texnoposedufinance.ui.main.MainFragmentDirections

class AdditionFragment: BaseFragment(R.layout.fragment_additions){

    private lateinit var binding: FragmentAdditionsBinding
    private lateinit var actBinding: ActionBarBinding
    private lateinit var navController: NavController
    private lateinit var parentNavController: NavController
    private val viewModel: SignInViewModel by viewModel()
    private val mGoogleSignInClient: GoogleSignInClient by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAdditionsBinding.bind(view)
        navController = Navigation.findNavController(view)
        actBinding = ActionBarBinding.bind(view)
        if (requireParentFragment().requireActivity() is MainActivity) {
            parentNavController = Navigation.findNavController(
                requireParentFragment().requireActivity() as MainActivity,
                R.id.nav_host
            )
        }
        actBinding.apply {
            tvTitle.text = context?.getString(R.string.addition)
        }
        binding.apply {
            teacher.onClick {
                val action = AdditionFragmentDirections.actionNavAdditionToTeacherFragment()
                navController.navigate(action)
            }
            student.onClick {
                val action = AdditionFragmentDirections.actionNavAdditionToStudentsFragment()
                navController.navigate(action)
            }
            info.onClick {
                val action = AdditionFragmentDirections.actionNavAdditionToInfoFragment()
                navController.navigate(action)
            }
            category.onClick {
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
            salary.onClick {
                val action = MainFragmentDirections.actionMainFragmentToSalaryFragment()
                parentNavController.navigate(action)
            }
        }
    }

}