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
import uz.texnopos.texnoposedufinance.databinding.FragmentEmployeeBinding


class TeacherFragment : BaseFragment(R.layout.fragment_employee) {

    private val adapter = TeacherAdapter()
    private val viewModel: TeacherViewModel by viewModel()

    private lateinit var navController: NavController
    private lateinit var binding: FragmentEmployeeBinding
    private lateinit var bindingActionBar: ActionBarBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentEmployeeBinding.bind(view)
        bindingActionBar = ActionBarBinding.bind(view)
        bindingActionBar.tvTitle.text = "Сотрудники"

        navController = Navigation.findNavController(view)
        binding.rcvTeachers.adapter = adapter
        setUpObservers()

        binding.srlTeachers.setOnRefreshListener {
            viewModel.getAllEmployees()
        }
        viewModel.getAllEmployees()
    }

    private fun setUpObservers() {
        viewModel.teacherList.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                ResourceState.SUCCESS -> {
                    if (it.data!!.isNotEmpty()) {
                        binding.tvEmptyList.visibility = View.GONE
                        binding.rcvTeachers.visibility = View.VISIBLE
                        adapter.models = it.data
                    } else {
                        binding.tvEmptyList.visibility = View.VISIBLE
                        binding.rcvTeachers.visibility = View.GONE
                    }
                    binding.srlTeachers.isRefreshing = false
                }
                ResourceState.ERROR -> {
                    toastLN(it.message)
                    binding.srlTeachers.isRefreshing = false
                }
                else -> {
                }
            }
        })
    }
}