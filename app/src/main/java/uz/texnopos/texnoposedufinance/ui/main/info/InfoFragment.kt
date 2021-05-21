package uz.texnopos.texnoposedufinance.ui.main.info

import android.os.Bundle
import android.view.View
import androidx.lifecycle.observe
import org.koin.android.viewmodel.ext.android.viewModel
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment
import uz.texnopos.texnoposedufinance.core.ResourceState
import uz.texnopos.texnoposedufinance.core.extentions.visibility
import uz.texnopos.texnoposedufinance.databinding.ActionBarBinding
import uz.texnopos.texnoposedufinance.databinding.FragmentInfoBinding

class InfoFragment: BaseFragment(R.layout.fragment_info) {
    lateinit var binding: FragmentInfoBinding
    lateinit var actBinding: ActionBarBinding
    private val viewModel: InfoViewModel by viewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentInfoBinding.bind(view)
        actBinding = ActionBarBinding.bind(view)
        viewModel.getOrgData()
        setUpObservers()
        actBinding.apply {
            tvTitle.text = context?.getString(R.string.about)
        }
    }
    private fun setUpObservers() {
        binding.apply {
            viewModel.org.observe(
                viewLifecycleOwner
            ) {
                when (it.status) {
                    ResourceState.LOADING -> {
                        loading.visibility = View.VISIBLE
                    }
                    ResourceState.SUCCESS -> {
                        loading.visibility(false)
                        tvName.text = it.data!!.name
                        tvAddress.text = it.data.address
                        tvBank.text = it.data.bank
                        tvInn.text = it.data.inn
                        tvMfo.text = it.data.mfo
                        tvPhone.text = it.data.phone
                        tvScore.text = it.data.score
                        tvDirector.text = it.data.director
                    }
                    ResourceState.ERROR -> {
                        loading.visibility(false)
                        toastLN(it.message)
                    }
                }
            }
        }
    }
}