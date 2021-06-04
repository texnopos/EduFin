package uz.texnopos.texnoposedufinance.ui.main.category.expense.my

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import org.koin.android.viewmodel.ext.android.viewModel
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment
import uz.texnopos.texnoposedufinance.core.ResourceState
import uz.texnopos.texnoposedufinance.core.extentions.onClick
import uz.texnopos.texnoposedufinance.core.extentions.visibility
import uz.texnopos.texnoposedufinance.databinding.ActionBarAddBinding
import uz.texnopos.texnoposedufinance.databinding.FragmentMyExpenseCategoryBinding
import uz.texnopos.texnoposedufinance.ui.main.category.CategoryViewModel

class MyExpenseCategoryFragment: BaseFragment(R.layout.fragment_my_expense_category){
    private lateinit var binding: FragmentMyExpenseCategoryBinding
    private lateinit var actBinding: ActionBarAddBinding
    private val adapter = MyExpenseCategoryAdapter()
    private val viewModel: CategoryViewModel by viewModel()
    private lateinit var navController: NavController
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMyExpenseCategoryBinding.bind(view)
        actBinding = ActionBarAddBinding.bind(view)
        navController = Navigation.findNavController(view)
        binding.apply {
            rcvCategory.adapter = adapter
            setUpObservers()
            viewModel.getAllExpenseCategories()
            adapter.setOnItemClickListener {
                val action = MyExpenseCategoryFragmentDirections.actionMyExpenseCategoryFragmentToAddExpenseFragment(it)
                navController.navigate(action)
                navController.popBackStack()
            }
        }
        actBinding.apply {
            actionBarTitle.text = context?.getString(R.string.addExpense)
            btnHome.onClick {
               navController.popBackStack()
            }
        }
    }
    private fun setUpObservers(){
        binding.apply {
            viewModel.expenseCategory.observe(viewLifecycleOwner, Observer {
                when(it.status){
                    ResourceState.LOADING ->{
                        loading.visibility(true)
                    }
                    ResourceState.SUCCESS ->{
                        loading.visibility(false)
                        adapter.models = it.data!!
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