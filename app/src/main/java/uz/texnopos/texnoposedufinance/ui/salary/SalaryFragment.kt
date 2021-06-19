package uz.texnopos.texnoposedufinance.ui.salary

import android.os.Bundle
import android.view.View
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment
import uz.texnopos.texnoposedufinance.databinding.ActionBarAddBinding
import uz.texnopos.texnoposedufinance.databinding.FragmentSalaryBinding

class SalaryFragment: BaseFragment(R.layout.fragment_salary) {
    private lateinit var binding: FragmentSalaryBinding
    private lateinit var actBinding: ActionBarAddBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        actBinding = ActionBarAddBinding.bind(view)
        binding = FragmentSalaryBinding.bind(view)
        actBinding.apply {
            actionBarTitle.text = context?.getString(R.string.salary_x)
        }
    }
}