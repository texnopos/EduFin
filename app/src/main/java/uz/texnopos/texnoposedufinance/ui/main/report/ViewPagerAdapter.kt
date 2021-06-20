package uz.texnopos.texnoposedufinance.ui.main.report

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.texnopos.texnoposedufinance.core.Resource
import uz.texnopos.texnoposedufinance.core.ResourceState
import uz.texnopos.texnoposedufinance.data.AllReports
import uz.texnopos.texnoposedufinance.data.model.response.ReportResponse
import uz.texnopos.texnoposedufinance.ui.main.report.expense.ExpenseFragment
import uz.texnopos.texnoposedufinance.ui.main.report.income.IncomeFragment

class ViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private val expenseFragment = ExpenseFragment()
    private val incomeFragment = IncomeFragment()

    fun setReport(report: Resource<ReportResponse>) {
        expenseFragment.report.value = report
        incomeFragment.report.value = report
    }

    override fun getItemCount(): Int = 2
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                expenseFragment
            }
            else -> {
                incomeFragment
            }
        }
    }
}