package uz.texnopos.texnoposedufinance.ui.main.info.add

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import org.koin.android.viewmodel.ext.android.viewModel
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment
import uz.texnopos.texnoposedufinance.core.ResourceState
import uz.texnopos.texnoposedufinance.core.extentions.onClick
import uz.texnopos.texnoposedufinance.core.extentions.visibility
import uz.texnopos.texnoposedufinance.databinding.ActionBarAddBinding
import uz.texnopos.texnoposedufinance.databinding.FragmentAddInfoBinding
import uz.texnopos.texnoposedufinance.ui.main.info.InfoViewModel

class AddInfoFragment : BaseFragment(R.layout.fragment_add_info) {
    private lateinit var binding: FragmentAddInfoBinding
    private lateinit var actBinding: ActionBarAddBinding
    private val viewModel: InfoViewModel by viewModel()
    private lateinit var navController: NavController
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddInfoBinding.bind(view)
        actBinding = ActionBarAddBinding.bind(view)
        navController = Navigation.findNavController(view)
        setUpObservers()
        viewModel.getOrgData()
        binding.apply {
            btnSave.onClick {
                val name = etOrganization.text.toString()
                val address = etAddress.text.toString()
                val inn = etInn.text.toString()
                val mfo = etMfo.text.toString()
                val phone = etPhone.text.toString()
                val score = etScore.text.toString()
                val director = etDirector.text.toString()
                val bank = etBank.text.toString()
                if (name.isEmpty()) etOrganization.error = context?.getString(R.string.fillField)
                if (address.isEmpty()) etAddress.error = context?.getString(R.string.fillField)
                if (inn.isEmpty()) etInn.error = context?.getString(R.string.fillField)
                if (mfo.isEmpty()) etMfo.error = context?.getString(R.string.fillField)
                if (phone.isEmpty()) etPhone.error = context?.getString(R.string.fillField)
                if (score.isEmpty()) etScore.error = context?.getString(R.string.fillField)
                if (director.isEmpty()) etDirector.error = context?.getString(R.string.fillField)
                if(bank.isEmpty()) etBank.error = context?.getString(R.string.fillField)
                viewModel.updateOrgData(
                    name = name, address = address, inn = inn, mfo = mfo,
                    phone = phone, bank = bank, score = score, director = director
                )
                isLoading(true)
            }
        }
        actBinding.apply {
            actionBarTitle.text = context?.getString(R.string.info)
        }
    }

    private fun setUpObservers() {
        binding.apply {
            viewModel.orgUpdate.observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    ResourceState.LOADING -> {
                       isLoading(true)
                    }
                    ResourceState.SUCCESS -> {
                        isLoading(false)
                        toastLN(context?.getString(R.string.added_successfully))
                        navController.popBackStack()
                    }
                    ResourceState.ERROR -> {
                        isLoading(false)
                        toastLN(it.message)
                    }
                }
            })
            viewModel.org.observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    ResourceState.LOADING -> isLoading(true)
                    ResourceState.SUCCESS -> {
                        etBank.setText(it.data!!.bank)
                        etDirector.setText(it.data.director)
                        etScore.setText(it.data.score)
                        etPhone.setText(it.data.phone)
                        etMfo.setText(it.data.mfo)
                        etOrganization.setText(it.data.name)
                        etInn.setText(it.data.inn)
                        etAddress.setText(it.data.address)
                        isLoading(false)
                    }
                    ResourceState.ERROR -> {
                        toastLN(it.message)
                        isLoading(false)
                    }
                }
            })
        }
    }
    private fun isLoading(b: Boolean){
        binding.apply {
            etAddress.isEnabled = !b
            btnSave.isEnabled = !b
            etOrganization.isEnabled = !b
            etBank.isEnabled = !b
            etDirector.isEnabled = !b
            etInn.isEnabled = !b
            etMfo.isEnabled = !b
            etScore.isEnabled = !b
            loading.visibility(b)
        }
    }
}