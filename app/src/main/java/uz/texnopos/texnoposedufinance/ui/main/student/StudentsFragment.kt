package uz.texnopos.texnoposedufinance.ui.main.student

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import org.koin.android.viewmodel.ext.android.viewModel
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment
import uz.texnopos.texnoposedufinance.core.ResourceState
import uz.texnopos.texnoposedufinance.core.extentions.visibility
import uz.texnopos.texnoposedufinance.databinding.ActionBarBinding
import uz.texnopos.texnoposedufinance.databinding.FragmentStudentsBinding

class StudentsFragment: BaseFragment(R.layout.fragment_students) {
    lateinit var binding: FragmentStudentsBinding
    lateinit var actionBarBinding: ActionBarBinding
    private val viewModel: StudentsViewModel by viewModel()
    private val adapter = StudentsAdapter()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentStudentsBinding.bind(view)
        actionBarBinding = ActionBarBinding.bind(view)
        setUpObservers()
        binding.apply {
            rcvStudents.adapter = adapter
            srlStudents.setOnRefreshListener {
                viewModel.getAllStudents()
                loading.visibility(false)
            }
        }
        actionBarBinding.apply {
            tvTitle.text = context?.getString(R.string.students)
        }

        viewModel.getAllStudents()
    }
    private fun setUpObservers(){
        binding.apply {
            viewModel.studentList.observe(viewLifecycleOwner, Observer {
                when(it.status){
                    ResourceState.LOADING ->{
                        srlStudents.isRefreshing = false
                        loading.visibility(true)
                    }
                    ResourceState.SUCCESS ->{
                        srlStudents.isRefreshing = false
                        adapter.models = it.data!!
                        loading.visibility(false)
                    }
                    ResourceState.ERROR ->{
                        srlStudents.isRefreshing = false
                        toastLN(it.message)
                        loading.visibility(false)
                    }
                }
            })
        }
    }
}