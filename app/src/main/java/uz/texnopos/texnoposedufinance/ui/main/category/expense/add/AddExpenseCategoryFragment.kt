package uz.texnopos.texnoposedufinance.ui.main.category.expense.add

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
import uz.texnopos.texnoposedufinance.databinding.ItemAddExpenseCategoryBinding
import uz.texnopos.texnoposedufinance.ui.main.category.AddCategoryFragment
import uz.texnopos.texnoposedufinance.ui.main.category.CategoryViewModel

class AddExpenseCategoryFragment(private val fr: AddCategoryFragment): BaseFragment(R.layout.item_add_expense_category){
    private lateinit var binding: ItemAddExpenseCategoryBinding
    val viewModel: CategoryViewModel by viewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ItemAddExpenseCategoryBinding.bind(view)
        binding.apply {
            setUpObservers()
            btnSave.onClick {
                val name = etCategoryName.text.toString()
                if(name.isEmpty()) etCategoryName.error = context?.getString(R.string.fillField)
                if(name.isNotEmpty()){
                    viewModel.addExpenseCategory(name)
                }
            }
        }
    }
    private fun setUpObservers(){
        binding.apply {
            viewModel.addExpenseCategory.observe(viewLifecycleOwner, Observer {
                when(it.status){
                    ResourceState.LOADING ->{
                        isLoading(true)
                    }
                    ResourceState.SUCCESS ->{
                        isLoading(false)
                        toastLN(context?.getString(R.string.added_successfully))
                        viewModel.getAllIncomeCategories()
                        fr.navController.popBackStack()
                    }
                    ResourceState.ERROR ->{
                        isLoading(false)
                        toastLN(it.message)
                    }
                }
            })
        }
    }
    fun isLoading(b: Boolean){
        binding.apply {
            btnSave.isEnabled = !b
            etCategoryName.isEnabled = !b
            loading.visibility(b)
        }
    }
}