package uz.texnopos.texnoposedufinance.ui.main.report

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.texnopos.texnoposedufinance.ui.main.MainFragment

class ReportsAdapter (
    val frg: MainFragment,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val date: Long, private val today: Long
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 3/*((today - date)/(3600000 * 24) + 1).toInt()*/
    override fun createFragment(position: Int): Fragment {
        return ReportsViewPagerFragment(frg)
    }
}