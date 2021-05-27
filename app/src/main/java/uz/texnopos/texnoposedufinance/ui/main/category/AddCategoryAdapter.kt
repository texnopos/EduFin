package uz.texnopos.texnoposedufinance.ui.main.category

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.texnopos.texnoposedufinance.ui.main.category.expense.add.AddExpenseCategoryFragment
import uz.texnopos.texnoposedufinance.ui.main.category.income.add.AddIncomeCategoryFragment

class AddCategoryAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 2
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                AddExpenseCategoryFragment()
            }
            else -> {
                AddIncomeCategoryFragment()
            }
        }
    }
}