package uz.texnopos.texnoposedufinance.ui.main.category

import android.os.Bundle
import android.view.View
import androidx.core.view.get
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.android.viewmodel.ext.android.viewModel
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment
import uz.texnopos.texnoposedufinance.databinding.ActionBarBinding
import uz.texnopos.texnoposedufinance.databinding.FragmentCategoryBinding
import uz.texnopos.texnoposedufinance.ui.main.MainFragment

class CategoryFragment: BaseFragment(R.layout.fragment_category) {
    private lateinit var binding: FragmentCategoryBinding
    private lateinit var navController: NavController
    private lateinit var adapter: CategoryAdapter
    private lateinit var actBinding: ActionBarBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCategoryBinding.bind(view)
        actBinding = ActionBarBinding.bind(view)
        navController = Navigation.findNavController(view)
        adapter = CategoryAdapter(requireActivity().supportFragmentManager, lifecycle)
        actBinding.apply {
            tvTitle.text = context?.getString(R.string.category)
        }
        binding.apply {
            vpAddCategory.adapter = adapter
            vpAddCategory.setPageTransformer { _, _ ->
                when(vpAddCategory.currentItem){
                    0 -> (requireParentFragment().requireParentFragment() as MainFragment).pos = 0
                    else -> (requireParentFragment().requireParentFragment() as MainFragment).pos = 1
                }
            }
            TabLayoutMediator(tlAddCategory, vpAddCategory){tab, position ->
                when(position){
                    0 -> {
                        tab.text = context?.getString(R.string.s_expenses)
                    }
                    else -> {
                        tab.text = context?.getString(R.string.s_incomes)
                    }
                }
            }.attach()
        }
    }
}