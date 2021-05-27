package uz.texnopos.texnoposedufinance.ui.main.category.expense

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import org.koin.android.viewmodel.ext.android.viewModel
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment
import uz.texnopos.texnoposedufinance.core.ResourceState
import uz.texnopos.texnoposedufinance.core.extentions.visibility
import uz.texnopos.texnoposedufinance.databinding.FragmentExpenseCategoryBinding
import uz.texnopos.texnoposedufinance.ui.main.category.CategoryViewModel

class ExpenseCategoryFragment: BaseFragment(R.layout.fragment_expense_category){
    lateinit var binding: FragmentExpenseCategoryBinding
    val adapter = ExpenseCategoryAdapter()
    private val viewModel: CategoryViewModel by viewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentExpenseCategoryBinding.bind(view)
        binding.apply {
            rcvCategory.adapter = adapter
            setUpObservers()
            viewModel.getAllExpenseCategories()
            srlCategory.setOnRefreshListener {
                srlCategory.isRefreshing = false
                viewModel.getAllExpenseCategories()
            }
        }
    }
    private fun setUpObservers(){
        binding.apply {
            viewModel.expenseCategory.observe(viewLifecycleOwner, Observer {
                when(it.status){
                    ResourceState.LOADING ->{
                        srlCategory.isRefreshing = false
                        loading.visibility(true)
                    }
                    ResourceState.SUCCESS ->{
                        srlCategory.isRefreshing = false
                        loading.visibility(false)
                        adapter.models = it.data!!
                        if(it.data.isEmpty()){
                            rcvCategory.visibility(false)
                            tvEmptyList.visibility(true)
                        }
                        else{
                            rcvCategory.visibility(true)
                            tvEmptyList.visibility(false)
                        }
                    }
                    ResourceState.ERROR ->{
                        toastLN(it.message)
                        srlCategory.isRefreshing = false
                        loading.visibility(false)
                    }
                }
            })
        }
    }
}