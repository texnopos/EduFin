package uz.texnopos.texnoposedufinance.ui.main.report

import android.os.Bundle
import android.view.View
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment
import uz.texnopos.texnoposedufinance.core.extentions.onClick
import uz.texnopos.texnoposedufinance.databinding.ItemReportsBinding
import uz.texnopos.texnoposedufinance.ui.main.MainFragment

class ReportsViewPagerFragment(private val frg: MainFragment): BaseFragment(R.layout.item_reports){
    private lateinit var binding: ItemReportsBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ItemReportsBinding.bind(view)
        binding.apply {
            clExpense.onClick {
                clExpense.setBackgroundResource(R.drawable.selector_square)
                clIncome.setBackgroundResource(R.drawable.shape_form)
                frg.temp = 0
            }
            clIncome.onClick {
                clIncome.setBackgroundResource(R.drawable.selector_square)
                clExpense.setBackgroundResource(R.drawable.shape_form)
                frg.temp = 1
            }
        }
    }
}