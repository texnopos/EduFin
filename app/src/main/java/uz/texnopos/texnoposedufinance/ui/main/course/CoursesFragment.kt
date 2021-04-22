package uz.texnopos.texnoposedufinance.ui.main.course

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import uz.texnopos.texnoposedufinance.MainActivity
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment
import uz.texnopos.texnoposedufinance.core.ResourceState
import uz.texnopos.texnoposedufinance.core.extentions.visibility
import uz.texnopos.texnoposedufinance.data.model.Course
import uz.texnopos.texnoposedufinance.data.model.request.NetworkHelper
import uz.texnopos.texnoposedufinance.data.retrofit.ApiInterface
import uz.texnopos.texnoposedufinance.databinding.ActionBarBinding
import uz.texnopos.texnoposedufinance.databinding.FragmentCoursesBinding
import uz.texnopos.texnoposedufinance.ui.main.MainFragmentDirections

class CoursesFragment: BaseFragment(R.layout.fragment_courses) {

    private val viewModel: CoursesViewModel by viewModel()
    private val adapter = CoursesAdapter()
    private lateinit var binding: FragmentCoursesBinding
    lateinit var actBinding: ActionBarBinding
    lateinit var navController: NavController
    lateinit var parentNavController: NavController
    private val auth: FirebaseAuth by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentCoursesBinding.bind(view)
        actBinding = ActionBarBinding.bind(view)
        navController = Navigation.findNavController(view)

        binding.rcvCourses.adapter = adapter
        actBinding.tvTitle.text = view.context.getString(R.string.courses)

        val orgId = auth.currentUser!!.uid
        setUpObservers()
        viewModel.getAllCourses(orgId)

        adapter.onResponse {
            adapter.models = it
        }
        adapter.onFailure {
            toastLN(it)
        }
        adapter.setAddGroupClicked {
            val action = MainFragmentDirections.actionMainFragmentToAddGroupFragment(it)
            parentNavController.navigate(action)
        }
        if (requireParentFragment().requireActivity() is MainActivity) {
            parentNavController = Navigation.findNavController(
                requireParentFragment().requireActivity() as
                        MainActivity, R.id.nav_host
            )
        }

        binding.swlCourses.setOnRefreshListener {
            viewModel.getAllCourses(orgId)
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
