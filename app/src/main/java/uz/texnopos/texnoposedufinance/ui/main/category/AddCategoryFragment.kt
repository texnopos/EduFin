package uz.texnopos.texnoposedufinance.ui.main.category

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment
import uz.texnopos.texnoposedufinance.core.extentions.onClick
import uz.texnopos.texnoposedufinance.databinding.ActionBarAddBinding
import uz.texnopos.texnoposedufinance.databinding.FragmentAddCategoryBinding

class AddCategoryFragment: BaseFragment(R.layout.fragment_add_category) {
    private lateinit var binding: FragmentAddCategoryBinding
    lateinit var navController: NavController
    private lateinit var adapter: AddCategoryAdapter
    private lateinit var actBinding: ActionBarAddBinding
    private val args: AddCategoryFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddCategoryBinding.bind(view)
        actBinding = ActionBarAddBinding.bind(view)
        navController = Navigation.findNavController(view)
        val pos = args.position
        adapter = AddCategoryAdapter(this, requireActivity().supportFragmentManager, lifecycle)
        actBinding.apply {
            actionBarTitle.text = context?.getString(R.string.add_category)
            btnHome.onClick {
                navController.popBackStack()
            }
        }
        binding.apply {
            vpAddCategory.adapter = adapter
            vpAddCategory.setCurrentItem(pos, false)
            TabLayoutMediator(tlAddCategory, vpAddCategory) { tab, position ->
                when (position) {
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