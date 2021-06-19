package uz.texnopos.texnoposedufinance.ui.main.report

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.texnopos.texnoposedufinance.ui.main.report.expense.ExpenseFragment
import uz.texnopos.texnoposedufinance.ui.main.report.income.IncomeFragment

class ViewPagerAdapter (fragmentManager: FragmentManager, lifecycle: Lifecycle, private val fromDate: Long, private val toDate: Long, val fr: ReportsFragment):
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 2
    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> {
                ExpenseFragment(fromDate, toDate, fr)
            }
            else -> {
                IncomeFragment(fromDate, toDate, fr)
            }
        }
    }
}