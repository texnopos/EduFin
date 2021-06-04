package uz.texnopos.texnoposedufinance.ui.main.report

import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayoutMediator
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment
import uz.texnopos.texnoposedufinance.databinding.ActionBarBinding
import uz.texnopos.texnoposedufinance.databinding.FragmentReportsBinding
import uz.texnopos.texnoposedufinance.ui.main.MainFragment
import java.util.*

class ReportsFragment: BaseFragment(R.layout.fragment_reports){
    private lateinit var binding: FragmentReportsBinding
    private lateinit var adapter: ReportsAdapter
    private lateinit var actBinding: ActionBarBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentReportsBinding.bind(view)
        actBinding = ActionBarBinding.bind(view)
        actBinding.apply {
            tvTitle.text = context?.getString(R.string.reports)
        }
        val date = 1621450800000
        val today = 1622177135178
        adapter = ReportsAdapter(requireParentFragment().requireParentFragment() as MainFragment, requireActivity().supportFragmentManager, lifecycle, date, today /*Calendar.getInstance().timeInMillis*/)
        binding.apply {
            vpAddCategory.adapter = adapter
            /*val map = mutableMapOf<Long, String>()
            var k = date
            repeat(((today - date)/(3600000 * 24) + 1).toInt()){
                    map[k] = Date(k).toLocaleString()
                    k += 3600000
                }*/
            TabLayoutMediator(tlAddCategory, vpAddCategory) { tab, position ->
                when(position){
                    0->{
                        tab.text = "1"
                    }
                    1->{
                        tab.text = "2"
                    }
                    else ->{
                        tab.text = "3"
                    }
                }
            }.attach()
        }
    }
}