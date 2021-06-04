package uz.texnopos.texnoposedufinance.ui.main.report.expense.add


import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import org.koin.android.viewmodel.ext.android.viewModel
import uz.texnopos.texnoposedufinance.MainActivity
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment
import uz.texnopos.texnoposedufinance.core.ResourceState
import uz.texnopos.texnoposedufinance.core.extentions.onClick
import uz.texnopos.texnoposedufinance.core.extentions.visibility
import uz.texnopos.texnoposedufinance.databinding.FragmentAddExpenseBinding
import uz.texnopos.texnoposedufinance.ui.main.group.add.CalendarDialog
import uz.texnopos.texnoposedufinance.ui.main.report.ReportsViewModel
import java.util.*

class AddExpenseFragment: BaseFragment(R.layout.fragment_add_expense) {
    private lateinit var binding: FragmentAddExpenseBinding
    private lateinit var navController: NavController
    private lateinit var parentNavController: NavController
    private var createdDate: Long = 0
    private var time: Long = 0
    private lateinit var category: String
    private val viewModel: ReportsViewModel by viewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddExpenseBinding.bind(view)
        navController = Navigation.findNavController(view)
        if (requireParentFragment().requireActivity() is MainActivity) {
            parentNavController = Navigation.findNavController(
                requireParentFragment().requireActivity() as
                        MainActivity, R.id.nav_host
            )
        }
        binding.apply {
            etCategory.onClick {
                /*val action = AddExpenseFragmentDirections.actionAddExpenseFragmentToMyExpenseCategoryFragment()
                parentNavController.navigate(action)*/
            }
            val args: AddExpenseFragmentArgs by navArgs()
            category = args.name
            etCategory.setText(category)
            etTime.onClick {
                val dialog = CalendarDialog(requireContext())
                dialog.show()
                createdDate = Calendar.getInstance().timeInMillis
                dialog.binding.apply {
                    btnYes.onClick {
                        val y = cvCalendar.year
                        val m = cvCalendar.month
                        val d = cvCalendar.dayOfMonth
                        val yStr = y.toString()
                        var mStr = (m + 1).toString()
                        var dStr = d.toString()
                        if (dStr.length != 2) dStr = "0$dStr"
                        if (mStr.length != 2) mStr = "0$mStr"
                        val cal = Calendar.getInstance()
                        cal.set(Calendar.DAY_OF_MONTH, d)
                        cal.set(Calendar.MONTH, m)
                        cal.set(Calendar.YEAR, y)
                        time = cal.timeInMillis
                        etTime.setText("$dStr.$mStr.$yStr")
                        dialog.dismiss()
                    }
                    btnCancel.onClick {
                        dialog.dismiss()
                    }
                }
            }

        }
    }
    private fun setUpObservers(){
        binding.apply {
            viewModel.expense.observe(viewLifecycleOwner, Observer {
                when(it.status){
                    ResourceState.LOADING ->{
                        loading.visibility(true)
                    }
                    ResourceState.SUCCESS ->{
                        loading.visibility(false)
                    }
                    ResourceState.ERROR ->{
                        toastLN(it.message)
                        loading.visibility(false)
                    }
                }
            })
        }
    }
}