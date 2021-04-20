package uz.texnopos.texnoposedufinance.ui.main.group.add

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import org.koin.android.ext.android.inject
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment
import uz.texnopos.texnoposedufinance.core.ResourceState
import uz.texnopos.texnoposedufinance.core.extentions.onClick
import uz.texnopos.texnoposedufinance.core.extentions.visibility
import uz.texnopos.texnoposedufinance.data.model.Day
import uz.texnopos.texnoposedufinance.databinding.AddActionBarBinding
import uz.texnopos.texnoposedufinance.databinding.FragmentAddGroupBinding

class AddGroupFragment: BaseFragment(R.layout.fragment_add_group), AdapterView.OnItemClickListener {
    private val viewModel: AddGroupViewModel by inject()
    private lateinit var binding: FragmentAddGroupBinding
    private lateinit var bindingActBar: AddActionBarBinding
    lateinit var navController: NavController
    var id = ""
    var teacher = ""
    var courseTime = ""
    var startDate = ""
    var courseId = ""

    private val safeArgs: AddGroupFragmentArgs by navArgs()


    private val daysAdapter = DaysAdapter()
    private val days = arrayListOf("П", "В", "С", "Ч", "П", "С", "В")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAddGroupBinding.bind(view)
        bindingActBar = AddActionBarBinding.bind(view)
        val adapter2 = ArrayAdapter(requireContext(), R.layout.spinner_item, arrayListOf<String>())

        bindingActBar.actionBarTitle.text = view.context.getString(R.string.create_group)
        navController = Navigation.findNavController(view)

        setUpObserversGroup()
        viewModel.getAllTeachers()

        binding.apply {
            timePicker.setIs24HourView(true)
            teachers.adapter = adapter2
            viewModel.teacherList.observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    ResourceState.LOADING -> loading.visibility(true)
                    ResourceState.SUCCESS -> {
                        loading.visibility(false)
                        adapter2.clear()
                        adapter2.addAll(it.data!!.map { e -> e.name })
                    }
                    ResourceState.ERROR -> {
                        loading.visibility(false)
                        toastLN(it.message)
                    }
                }
            })
            teachers.onItemSelectedListener =
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
                    }
                }

            rcvDays.adapter = daysAdapter
            val models = mutableListOf<Day>()
            for (i in 0..6) {
                models.add(Day(days[i], false))
            }
            daysAdapter.models = models

            btnAdd.onClick {
                val lessonDays = date.text.toString()
                courseTime = if (timePicker.minute == 0) {
                    "${timePicker.hour}:${timePicker.minute}0"
                } else "${timePicker.hour}:${timePicker.minute}"
                courseId = safeArgs.id
                val name = groupName.text.toString()
                if(name.isEmpty()) groupName.error = requireContext().getString(R.string.fillField)
                viewModel.createGroup(
                    name, teacher, courseId, courseTime, startDate, lessonDays)
            }
        }
        bindingActBar.btnHome.onClick {
            navController.popBackStack()
        }
    }

    private fun setUpObserversGroup() {
        binding.apply {
            viewModel.createGroup.observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    ResourceState.LOADING -> {
                        loading.visibility(true)
                    }
                    ResourceState.SUCCESS -> {
                        loading.visibility(false)
                        toastLNCenter("Added successfully")
                    }
                    ResourceState.ERROR -> {
                        loading.visibility(false)
                        toastLN(it.message)
                    }
                }
            })
        }

    }

    @SuppressLint("ResourceAsColor")
    fun isSelected(view: TextView) {
        view.setBackgroundResource(R.drawable.shape_circle)
        view.setTextColor(R.color.purple_500)
    }

    @SuppressLint("ResourceAsColor")
    fun isNotSelected(view: TextView) {
        view.setBackgroundResource(R.drawable.shape_circle)
        view.setTextColor(R.color.black)
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        TODO("Not yet implemented")
    }
}

