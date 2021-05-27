package uz.texnopos.texnoposedufinance.ui.main.category.income

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import org.koin.android.viewmodel.ext.android.viewModel
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment
import uz.texnopos.texnoposedufinance.core.ResourceState
import uz.texnopos.texnoposedufinance.core.extentions.visibility
import uz.texnopos.texnoposedufinance.databinding.FragmentIncomeCategoryBinding
import uz.texnopos.texnoposedufinance.ui.main.category.CategoryViewModel

class IncomeCategoryFragment : BaseFragment(R.layout.fragment_income_category) {
    lateinit var binding: FragmentIncomeCategoryBinding
    val adapter = IncomeCategoryAdapter()
    private val viewModel: CategoryViewModel by viewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentIncomeCategoryBinding.bind(view)
        binding.apply {
            rcvCategory.adapter = adapter
            setUpObservers()
            viewModel.getAllIncomeCategories()
            srlCategory.setOnRefreshListener {
                srlCategory.isRefreshing = false
                viewModel.getAllIncomeCategories()
            }
        }
    }

    private fun setUpObservers() {
        binding.apply {
            viewModel.incomeCategory.observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    ResourceState.LOADING -> {
                        srlCategory.isRefreshing = false
                        loading.visibility(true)
                    }
                    ResourceState.SUCCESS -> {
                        srlCategory.isRefreshing = false
                        loading.visibility(false)
                        adapter.models = it.data!!
                        if (it.data.isEmpty()) {
                            rcvCategory.visibility(false)
                            tvEmptyList.visibility(true)
                        } else {
                            rcvCategory.visibility(true)
                            tvEmptyList.visibility(false)
                        }
                    }
                    ResourceState.ERROR -> {
                        toastLN(it.message)
                        srlCategory.isRefreshing = false
                        loading.visibility(false)
                    }
                }
            })
        }
    }
}