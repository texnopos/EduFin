package uz.texnopos.texnoposedufinance.ui.main.course.add

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import org.koin.android.viewmodel.ext.android.viewModel
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment
import uz.texnopos.texnoposedufinance.core.ResourceState
import uz.texnopos.texnoposedufinance.core.extentions.onClick
import uz.texnopos.texnoposedufinance.core.extentions.visibility
import uz.texnopos.texnoposedufinance.databinding.AddActionBarBinding
import uz.texnopos.texnoposedufinance.databinding.FragmentAddCoursesBinding

class AddCoursesFragment : BaseFragment(R.layout.fragment_add_courses),
    AdapterView.OnItemClickListener {

    private val viewModel: AddCoursesViewModel by viewModel()
    lateinit var binding: FragmentAddCoursesBinding
    lateinit var bindingActBar: AddActionBarBinding
    lateinit var navController: NavController
    private var teacher: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item, arrayListOf<String>())

        binding = FragmentAddCoursesBinding.bind(view)

        bindingActBar = AddActionBarBinding.bind(view)
        bindingActBar.actionBarTitle.text = view.context.getString(R.string.create_group)

        navController = Navigation.findNavController(view)
        setUpObserversCourse()

        viewModel.getAllTeachers()

        viewModel.teacherList.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                ResourceState.LOADING -> binding.loading.visibility(true)
                ResourceState.SUCCESS -> {
                    binding.loading.visibility(false)
                    adapter.clear()
                    adapter.addAll(it.data!!.map { e -> e.name })
                }
                ResourceState.ERROR -> {
                    binding.loading.visibility(false)
                    toastLN(it.message)
                }
            }
        })

        binding.teachers.adapter = adapter

        binding.teachers.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    teacher = ""
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    teacher = viewModel.teacherList.value?.data!![position].name
                    /*if (teacher.isNotEmpty() && binding.courseName.text!!.isNotEmpty()) {
                        binding.btnSave.enabled(true)
                    } else binding.btnSave.enabled(false)*/

                }
            }
        
        bindingActBar.btnHome.onClick {
            navController.popBackStack()
        }

        binding.btnSave.onClick {
            if(!binding.courseName.text.isNullOrEmpty() &&
                !binding.cost.text.isNullOrEmpty() && !binding.coursePeriod.text.isNullOrEmpty()){
                val name = binding.courseName.text.toString()
                val cost: Double = binding.cost.text.toString().toDouble()
                val period = binding.coursePeriod.text.toString().toInt()
                viewModel.createCourse(name, teacher, cost, period).toString()
            }
            else{
                if(binding.courseName.text.isNullOrEmpty()) binding.courseName.error = "error"
                if(binding.cost.text.isNullOrEmpty()) binding.cost.error = "error"
                if(binding.coursePeriod.text.isNullOrEmpty()) binding.coursePeriod.error = "error"
            }

        }
    }

    private fun setUpObserversCourse() {
        viewModel.createCourse.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                ResourceState.LOADING -> {
                    binding.loading.visibility(true)
                }
                ResourceState.SUCCESS -> {
                    binding.loading.visibility(false)
                    toastLNCenter("Added successfully")
                }
                ResourceState.ERROR -> {
                    binding.loading.visibility(false)
                    toastLN(it.message)
                }
            }
        })
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        //
    }
}
