package uz.texnopos.texnoposedufinance.ui.main.teacher

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import org.koin.android.viewmodel.ext.android.viewModel
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment
import uz.texnopos.texnoposedufinance.core.ResourceState
import uz.texnopos.texnoposedufinance.databinding.ActionBarBinding
import uz.texnopos.texnoposedufinance.databinding.FragmentTeachersBinding


class TeacherFragment : BaseFragment(R.layout.fragment_teachers) {

    private val adapter = TeacherAdapter()
    private val viewModel: TeacherViewModel by viewModel()

    private lateinit var navController: NavController
    private lateinit var binding: FragmentTeachersBinding
    private lateinit var bindingActionBar: ActionBarBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentTeachersBinding.bind(view)
        bindingActionBar = ActionBarBinding.bind(view)
        bindingActionBar.tvTitle.text = view.context.getString(R.string.teachers)

        navController = Navigation.findNavController(view)
        binding.rcvTeachers.adapter = adapter

        setUpObservers()
        binding.apply {
            srlTeachers.setOnRefreshListener {
                viewModel.getAllEmployees()
            }
        }

        viewModel.getAllEmployees()
    }

    private fun setUpObservers() {
        binding.apply {
            viewModel.teacherList.observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    ResourceState.SUCCESS -> {
                        if (it.data!!.isNotEmpty()) {
                            tvEmptyList.visibility = View.GONE
                            rcvTeachers.visibility = View.VISIBLE
                            adapter.models = it.data
                        } else {
                            tvEmptyList.visibility = View.VISIBLE
                            rcvTeachers.visibility = View.GONE
                        }
                        srlTeachers.isRefreshing = false
                    }
                    ResourceState.ERROR -> {
                        toastLN(it.message)
                        srlTeachers.isRefreshing = false
                    }
                    else -> {
                    }
                }
            })
        }
    }
}