package uz.texnopos.texnoposedufinance.ui.main.group.select

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import org.koin.android.viewmodel.ext.android.viewModel
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment
import uz.texnopos.texnoposedufinance.core.ResourceState
import uz.texnopos.texnoposedufinance.core.extentions.visibility
import uz.texnopos.texnoposedufinance.databinding.ActionBarBinding
import uz.texnopos.texnoposedufinance.databinding.FragmentSelectStudentsBinding
import uz.texnopos.texnoposedufinance.ui.main.student.StudentsViewModel

class SelectStudentsFragment: BaseFragment(R.layout.fragment_select_students) {
    lateinit var binding: FragmentSelectStudentsBinding
    lateinit var actBinding: ActionBarBinding
    private val adapter = SelectStudentsAdapter()
    private val viewModel: StudentsViewModel by viewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSelectStudentsBinding.bind(view)
        actBinding = ActionBarBinding.bind(view)
        setUpObservers()
        actBinding.apply {
            tvTitle.text = context?.getString(R.string.selectStudents)
        }
        binding.apply {
            rcvStudents.adapter = adapter
            loading.visibility(false)
            srlStudents.setOnRefreshListener {
                viewModel.getSelectStudents("")
                loading.visibility(false)
            }
        }
        viewModel.getSelectStudents("")
    }

    private fun setUpObservers() {
        binding.apply {
            viewModel.selectStudentsList.observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    ResourceState.LOADING -> {
                        srlStudents.isRefreshing = false
                        loading.visibility(true)
                    }
                    ResourceState.SUCCESS -> {
                        srlStudents.isRefreshing = false
                        if (it.data!!.isNotEmpty()) {
                            tvEmptyList.visibility = View.GONE
                            rcvStudents.visibility = View.VISIBLE
                            adapter.models = it.data
                        } else {
                            tvEmptyList.visibility = View.VISIBLE
                            rcvStudents.visibility = View.GONE
                        }
                        loading.visibility(false)
                    }
                    ResourceState.ERROR -> {
                        srlStudents.isRefreshing = false
                        toastLN(it.message)
                        loading.visibility(false)
                    }
                }
            })
        }
    }
}