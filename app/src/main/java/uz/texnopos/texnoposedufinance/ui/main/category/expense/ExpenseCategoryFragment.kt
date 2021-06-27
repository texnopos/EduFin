package uz.texnopos.texnoposedufinance.ui.main.category.expense

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import org.koin.android.viewmodel.ext.android.viewModel
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment
import uz.texnopos.texnoposedufinance.core.RealtimeChangesResourceState
import uz.texnopos.texnoposedufinance.core.ResourceState
import uz.texnopos.texnoposedufinance.core.extentions.visibility
import uz.texnopos.texnoposedufinance.databinding.FragmentExpenseCategoryBinding
import uz.texnopos.texnoposedufinance.ui.main.category.CategoryViewModel

class ExpenseCategoryFragment: BaseFragment(R.layout.fragment_expense_category){
    private lateinit var binding: FragmentExpenseCategoryBinding
    private val adapter = ExpenseCategoryAdapter()
    private val viewModel: CategoryViewModel by viewModel()
    private lateinit var category: String
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentExpenseCategoryBinding.bind(view)
        setUpObservers()
        binding.apply {
            rcvCategory.adapter = adapter
            srlCategory.setOnRefreshListener {
                srlCategory.isRefreshing = false
                loading.visibility(false)
                viewModel.getAllExpenseCategories()
            }
            rcvCategory.addItemDecoration(DividerItemDecoration(root.context, DividerItemDecoration.VERTICAL))
        }
        viewModel.getAllExpenseCategories()
        adapter.setOnItemClickListener {
            category = it
        }
    }
    private fun setUpObservers(){
        binding.apply {
            viewModel.expenseCategory.observe(viewLifecycleOwner, Observer {
                when(it.status){
                    RealtimeChangesResourceState.LOADING -> {
                        loading.visibility(true)
                    }
                    RealtimeChangesResourceState.ADDED -> {
                        loading.visibility(false)
                        adapter.onAdded(it.data!!)
                    }
                    RealtimeChangesResourceState.MODIFIED -> {
                        loading.visibility(false)
                        adapter.onModified(it.data!!)
                    }
                    RealtimeChangesResourceState.REMOVED -> {
                        loading.visibility(false)
                        adapter.onRemoved(it.data!!)
                    }
                    RealtimeChangesResourceState.ERROR -> {
                        toastLN(it.message)
                        loading.visibility(false)
                    }
                }
            })
        }
    }
}