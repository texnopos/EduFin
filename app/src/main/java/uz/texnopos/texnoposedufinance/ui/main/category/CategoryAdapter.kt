package uz.texnopos.texnoposedufinance.ui.main.category

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.texnopos.texnoposedufinance.ui.main.MainFragment
import uz.texnopos.texnoposedufinance.ui.main.category.expense.ExpenseCategoryFragment
import uz.texnopos.texnoposedufinance.ui.main.category.income.IncomeCategoryFragment

class CategoryAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle):
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 2
    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> {
                ExpenseCategoryFragment()
            }
            else -> {
                IncomeCategoryFragment()
            }
        }
    }
}