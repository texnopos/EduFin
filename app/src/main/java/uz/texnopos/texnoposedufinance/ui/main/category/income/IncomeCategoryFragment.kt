package uz.texnopos.texnoposedufinance.ui.main.category.income

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import org.koin.android.viewmodel.ext.android.viewModel
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment
import uz.texnopos.texnoposedufinance.core.RealtimeChangesResourceState
import uz.texnopos.texnoposedufinance.core.extentions.visibility
import uz.texnopos.texnoposedufinance.databinding.FragmentIncomeCategoryBinding
import uz.texnopos.texnoposedufinance.ui.main.category.CategoryViewModel

class IncomeCategoryFragment : BaseFragment(R.layout.fragment_income_category) {
    private lateinit var binding: FragmentIncomeCategoryBinding
    private val adapter = IncomeCategoryAdapter()
    private val viewModel: CategoryViewModel by viewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentIncomeCategoryBinding.bind(view)
        binding.apply {
            rcvCategory.adapter = adapter
            setUpObservers()
            viewModel.getAllIncomeCategories()
            rcvCategory.addItemDecoration(DividerItemDecoration(root.context, DividerItemDecoration.VERTICAL))
        }
    }

    private fun setUpObservers() {
        binding.apply {
            viewModel.incomeCategory.observe(viewLifecycleOwner, Observer {
                when (it.status) {
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