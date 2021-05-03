package uz.texnopos.texnoposedufinance.ui.main.group.info

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import org.koin.android.ext.android.inject
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment
import uz.texnopos.texnoposedufinance.core.ResourceState
import uz.texnopos.texnoposedufinance.core.extentions.onClick
import uz.texnopos.texnoposedufinance.core.extentions.visibility
import uz.texnopos.texnoposedufinance.databinding.ActionBarAddBinding
import uz.texnopos.texnoposedufinance.databinding.FragmentGroupInfoBinding
import uz.texnopos.texnoposedufinance.ui.main.MainFragment
import uz.texnopos.texnoposedufinance.ui.main.group.GroupViewModel

class GroupInfoFragment : BaseFragment(R.layout.fragment_group_info) {
    private lateinit var binding: FragmentGroupInfoBinding
    private lateinit var actBinding: ActionBarAddBinding
    private lateinit var navController: NavController
    private val viewModel: GroupViewModel by inject()
    private val safeArgs: GroupInfoFragmentArgs by navArgs()
    lateinit var groupId: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        groupId = safeArgs.groupId
        (requireParentFragment().requireParentFragment() as MainFragment).groupId = groupId
        binding = FragmentGroupInfoBinding.bind(view)
        actBinding = ActionBarAddBinding.bind(view)
        navController = Navigation.findNavController(view)
        actBinding.apply {
            actionBarTitle.text = root.context.getString(R.string.addStudent)
            btnHome.onClick {
                navController.popBackStack()
            }
        }
        setUpObservers()
        //viewModel.getDatCurrentGroup(groupId)
        binding.apply {
            tvName.text = groupId
        }
    }

    private fun setUpObservers() {
        binding.apply {
            viewModel.group.observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    ResourceState.LOADING -> {
                        loading.visibility(true)
                    }
                    ResourceState.SUCCESS -> {
                        actBinding.actionBarTitle.text = it.data!!.name
                        tvName.text = it.data.name
                        tvDays.text = it.data.days
                        tvTime.text = it.data.time
                        loading.visibility(false)
                    }
                    ResourceState.ERROR -> {
                        toastLN(it.message)
                        loading.visibility(false)
                    }
                }
            })
        }
    }
}