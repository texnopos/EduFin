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
import uz.texnopos.texnoposedufinance.core.extentions.visibility
import uz.texnopos.texnoposedufinance.data.model.request.ApiClient
import uz.texnopos.texnoposedufinance.data.model.request.NetworkHelper
import uz.texnopos.texnoposedufinance.databinding.ActionBarBinding
import uz.texnopos.texnoposedufinance.databinding.FragmentCoursesBinding
import uz.texnopos.texnoposedufinance.ui.main.MainFragmentDirections

class CoursesFragment : BaseFragment(R.layout.fragment_courses) {

    private val viewModel: CoursesViewModel by viewModel()
    private val adapter = CoursesAdapter()
    private lateinit var binding: FragmentCoursesBinding
    lateinit var actBinding: ActionBarBinding
    lateinit var navController: NavController
    lateinit var parentNavController: NavController
    lateinit var networkHelper: NetworkHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentCoursesBinding.bind(view)
        actBinding = ActionBarBinding.bind(view)
        navController = Navigation.findNavController(view)
        networkHelper = NetworkHelper(ApiClient.getClient())

        setUpObservers()

        binding.rcvCourses.adapter = adapter
        actBinding.tvTitle.text = view.context.getString(R.string.courses)

        viewModel.getAllCourses()

        setData()
        adapter.onResponse {
            adapter.models = it
        }
        adapter.onFailure{
            toastLN(it)
        }
        adapter.setAddGroupClicked {
            val action = MainFragmentDirections.actionMainFragmentToAddGroupFragment(it)
            parentNavController.navigate(action)
        }

        /*adapter.setOnItemClicked {
            viewModel.getAllGroups(it)
            binding.apply {
                viewModel.groupList.observe(viewLifecycleOwner, Observer {u->
                    when (u.status) {
                        ResourceState.LOADING -> {

                        }
                        ResourceState.SUCCESS -> {
                            if (u.data!!.isNotEmpty()) {
                                adapter.groupAdapter.models = u.data
                                toastLN("Magliwmat aldi")
                            } else {
                                toastLN("Gruppalar joq")
                            }
                        }
                        ResourceState.ERROR -> {
                            toastLN(u.message)
                        }
                    }
                })
            }
        }*/

        if (requireParentFragment().requireActivity() is MainActivity) {
            parentNavController = Navigation.findNavController(
                    requireParentFragment().requireActivity() as
                            MainActivity, R.id.nav_host
                )
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
    fun setData(){
        networkHelper.getAllCourses(adapter)
    }
}
