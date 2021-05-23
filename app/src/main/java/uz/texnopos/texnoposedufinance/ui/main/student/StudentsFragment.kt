package uz.texnopos.texnoposedufinance.ui.main.student

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment
import uz.texnopos.texnoposedufinance.core.ResourceState
import uz.texnopos.texnoposedufinance.core.extentions.visibility
import uz.texnopos.texnoposedufinance.databinding.ActionBarBinding
import uz.texnopos.texnoposedufinance.databinding.FragmentStudentsBinding
import uz.texnopos.texnoposedufinance.ui.main.MainFragment

class StudentsFragment: BaseFragment(R.layout.fragment_students) {
    lateinit var binding: FragmentStudentsBinding
    lateinit var actionBarBinding: ActionBarBinding
    private val viewModel: StudentsViewModel by viewModel()
    private val adapter: StudentAdapter by inject()
    var passportList: ArrayList<String> = arrayListOf()
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
                        isLoading(true)
                    }
                    ResourceState.SUCCESS ->{
                        isLoading(false)
                        if(it.data!!.isNotEmpty()){
                            rcvStudents.visibility(true)
                            tvEmptyList.visibility(false)
                        }
                        else{
                            rcvStudents.visibility(false)
                            tvEmptyList.visibility(true)
                        }
                        adapter.models = it.data
                        it.data.forEach {student ->
                            passportList.add(student.passport)
                        }
                        (requireParentFragment().requireParentFragment() as MainFragment).passportList = passportList
                    }
                    ResourceState.ERROR ->{
                        isLoading(false)
                        toastLN(it.message)
                    }
                }
            })
        }
    }
    private fun isLoading(b: Boolean){
        binding.apply {
            loading.visibility(b)
            srlStudents.isRefreshing = false
            rcvStudents.visibility(!b)
        }
    }
}