package uz.texnopos.texnoposedufinance.ui.main.group

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import org.koin.android.viewmodel.ext.android.viewModel
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment
import uz.texnopos.texnoposedufinance.core.ResourceState
import uz.texnopos.texnoposedufinance.core.extentions.visibility
import uz.texnopos.texnoposedufinance.databinding.ItemCoursesBinding

class GroupFragment : BaseFragment(R.layout.item_courses) {
    private val adapter = GroupAdapter()
    private lateinit var binding: ItemCoursesBinding
    private val viewModel: GroupViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ItemCoursesBinding.bind(view)
        binding.rvGroups.adapter = adapter
        //viewModel.getAllGroups()
        setUpObservers()
    }


    private fun setUpObservers() {
        viewModel.groupResult.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                ResourceState.LOADING -> {
                    //
                }
                ResourceState.SUCCESS -> {
                    if (it.data!!.isNotEmpty()) {
                        binding.rvGroups.visibility(true)
                        adapter.models = it.data
                    } else {
                        binding.rvGroups.visibility(false)
                        toastLN("Gruppa joq")
                    }
                }
                ResourceState.ERROR -> {
                    toastLN(it.message)
                }
            }
        })
    }
}