package uz.texnopos.texnoposedufinance.ui.main.course

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import org.koin.android.viewmodel.ext.android.viewModel
import uz.texnopos.texnoposedufinance.MainActivity
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment
import uz.texnopos.texnoposedufinance.core.ResourceState
import uz.texnopos.texnoposedufinance.core.extentions.onClick
import uz.texnopos.texnoposedufinance.core.extentions.visibility
import uz.texnopos.texnoposedufinance.databinding.ActionBarAddBinding
import uz.texnopos.texnoposedufinance.databinding.FragmentCoursesBinding
import uz.texnopos.texnoposedufinance.ui.main.MainFragmentDirections
import uz.texnopos.texnoposedufinance.ui.main.group.GroupAdapter
import uz.texnopos.texnoposedufinance.ui.main.group.GroupViewModel

class CoursesFragment : BaseFragment(R.layout.fragment_courses) {

    private val viewModel: CoursesViewModel by viewModel()
    private val adapter = CoursesAdapter()
    private val gAdapter = GroupAdapter()
    private lateinit var binding: FragmentCoursesBinding
    lateinit var actBinding: ActionBarAddBinding
    lateinit var navController: NavController
    lateinit var parentNavController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentCoursesBinding.bind(view)
        actBinding = ActionBarAddBinding.bind(view)
        navController = Navigation.findNavController(view)

        setUpObservers()
        binding.rcvCourses.adapter = adapter
        actBinding.title.text = view.context.getString(R.string.courses)
        viewModel.getAllCourses()

        if (requireParentFragment().requireActivity() is MainActivity) {
            parentNavController = Navigation.findNavController(
                    requireParentFragment().requireActivity() as
                            MainActivity, R.id.nav_host
                )
        }

        actBinding.add.onClick {
            val action = MainFragmentDirections.actionMainFragmentToAddGroupFragment()
            parentNavController.navigate(action)
        }
        adapter.setOnItemClicked { id ->
            val groupViewModel: GroupViewModel by viewModel()
            groupViewModel.groupResult.observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    ResourceState.LOADING -> {
                        //
                    }
                    ResourceState.SUCCESS -> {
                        if (it.data!!.isNotEmpty()) {
                            gAdapter.models = it.data
                            toastLN("Magliwmat aldi")
                        } else {
                            toastLN("Gruppa joq")
                        }
                    }
                    ResourceState.ERROR -> {
                        toastLN(it.message)
                    }
                }
            })
            groupViewModel.getAllGroups(id)
        }
        binding.swlCourses.setOnRefreshListener {
            viewModel.getAllCourses()
        }
    }

    private fun setUpObservers() {
        binding.apply {
            viewModel.courseList.observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    ResourceState.LOADING -> swlCourses.isRefreshing = false
                    ResourceState.SUCCESS -> {
                        if (it.data!!.isNotEmpty()) {
                            tvEmptyList.visibility(false)
                            rcvCourses.visibility(true)
                            adapter.models = it.data
                        } else {
                            tvEmptyList.visibility(true)
                            rcvCourses.visibility(false)
                        }
                        swlCourses.isRefreshing = false
                    }
                    ResourceState.ERROR -> {
                        toastLN(it.message)
                        swlCourses.isRefreshing = false
                    }
                }
            })
        }
    }
}
