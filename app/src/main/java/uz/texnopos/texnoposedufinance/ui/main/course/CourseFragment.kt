package uz.texnopos.texnoposedufinance.ui.main.course

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.util.Assert
import com.google.gson.Gson
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import uz.texnopos.texnoposedufinance.MainActivity
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment
import uz.texnopos.texnoposedufinance.core.ResourceState
import uz.texnopos.texnoposedufinance.core.extentions.visibility
import uz.texnopos.texnoposedufinance.data.model.Course
import uz.texnopos.texnoposedufinance.data.model.Group
import uz.texnopos.texnoposedufinance.databinding.ActionBarBinding
import uz.texnopos.texnoposedufinance.databinding.FragmentCoursesBinding
import uz.texnopos.texnoposedufinance.ui.main.MainFragmentDirections

class CourseFragment: BaseFragment(R.layout.fragment_courses) {

    private val viewModel: CourseViewModel by viewModel()
    private val adapter: CourseAdapter by inject()
    private lateinit var binding: FragmentCoursesBinding
    lateinit var actBinding: ActionBarBinding
    lateinit var navController: NavController
    lateinit var parentNavController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentCoursesBinding.bind(view)
        actBinding = ActionBarBinding.bind(view)
        navController = Navigation.findNavController(view)
        setUpObservers()

        adapter.onResponse {
            adapter.models = it
        }
        adapter.setOnGroupItemClickListener {s, s1 ->
            val action = CourseFragmentDirections.actionNavCourseToGroupInfoFragment(s, s1)
            navController.navigate(action)
        }

        adapter.onFailure {
            toastLN(it)
        }
        actBinding.tvTitle.text = view.context.getString(R.string.courses)
        adapter.setAddGroupClicked{ s, s1 ->
            val action = MainFragmentDirections.actionMainFragmentToAddGroupFragment(s, s1)
            parentNavController.navigate(action)
        }

        if (requireParentFragment().requireActivity() is MainActivity) {
            parentNavController = Navigation.findNavController(
                requireParentFragment().requireActivity() as
                        MainActivity, R.id.nav_host
            )
        }
        binding.apply {
            swlCourses.setOnRefreshListener {
                viewModel.getAllCourses()
                loading.visibility(false)
            }
            rcvCourses.adapter = adapter
        }
        viewModel.getAllCourses()
    }

    private fun setUpObservers() {
        binding.apply {
            viewModel.courseList.observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    ResourceState.LOADING -> {
                        loading.visibility(true)
                        rcvCourses.visibility(false)
                    }
                    ResourceState.SUCCESS -> {
                        if (it.data!!.isNotEmpty()) {
                            tvEmptyList.visibility(false)
                            rcvCourses.visibility(true)
                            adapter.models = it.data
                        } else {
                            tvEmptyList.visibility(true)
                            rcvCourses.visibility(false)
                        }
                        loading.visibility(false)
                        swlCourses.isRefreshing = false
                    }
                    ResourceState.ERROR -> {
                        toastLN(it.message)
                        loading.visibility(false)
                        swlCourses.isRefreshing = false
                    }
                }
            })
        }
    }
}
