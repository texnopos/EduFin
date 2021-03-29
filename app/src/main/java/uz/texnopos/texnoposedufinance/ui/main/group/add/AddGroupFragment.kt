package uz.texnopos.texnoposedufinance.ui.main.group.add

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import org.koin.android.ext.android.inject
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment
import uz.texnopos.texnoposedufinance.core.ResourceState
import uz.texnopos.texnoposedufinance.core.extentions.onClick
import uz.texnopos.texnoposedufinance.core.extentions.visibility
import uz.texnopos.texnoposedufinance.databinding.AddActionBarBinding
import uz.texnopos.texnoposedufinance.databinding.FragmentAddGroupBinding

class AddGroupFragment : BaseFragment(R.layout.fragment_add_group), AdapterView.OnItemClickListener {
    private val viewModel: AddGroupViewModel by inject()
    private lateinit var binding: FragmentAddGroupBinding
    private lateinit var bindingActBar: AddActionBarBinding
    lateinit var navController: NavController
    var course = ""
    var id = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAddGroupBinding.bind(view)
        bindingActBar = AddActionBarBinding.bind(view)

        val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item, arrayListOf<String>())

        bindingActBar.actionBarTitle.text = view.context.getString(R.string.create_group)
        navController = Navigation.findNavController(view)

        setUpObserversGroup()

        viewModel.getAllCourses()

        binding.courses.adapter = adapter

        viewModel.courseList.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                ResourceState.LOADING -> binding.loading.visibility(true)
                ResourceState.SUCCESS -> {
                    binding.loading.visibility(false)
                    adapter.clear()
                    adapter.addAll(it.data!!.map { e -> e!!.name })
                    binding.courseId.text = ""
                }
                ResourceState.ERROR -> {
                    binding.loading.visibility(false)
                    toastLN(it.message)
                }
            }
        })

        binding.courses.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    course = ""
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    course = viewModel.courseList.value?.data!![position]!!.name
                    binding.courseId.text = viewModel.courseList.value?.data!![position]!!.id
                }
            }

        binding.btnAdd.onClick {
            if(binding.courseId.text.isNotEmpty() && binding.courseNum.text!!.isNotEmpty()){
                viewModel.createGroup(course, binding.courseNum.text!!.toString(), binding.courseId.text.toString())
            }
        }
        bindingActBar.btnHome.onClick{
            navController.popBackStack()
        }
    }

    private fun setUpObserversGroup() {
        viewModel.createGroup.observe(viewLifecycleOwner, Observer {
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
        TODO("Not yet implemented")
    }
}