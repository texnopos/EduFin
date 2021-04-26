package uz.texnopos.texnoposedufinance.ui.main.group.add

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.RequiresApi
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
import uz.texnopos.texnoposedufinance.databinding.ActionBar2Binding
import uz.texnopos.texnoposedufinance.databinding.FragmentAddGroupBinding


class AddGroupFragment: BaseFragment(R.layout.fragment_add_group), AdapterView.OnItemClickListener {
    private val viewModel: AddGroupViewModel by inject()
    private lateinit var binding: FragmentAddGroupBinding
    private lateinit var bindingActBar: ActionBar2Binding
    lateinit var navController: NavController
    var id = ""
    var teacher = ""
    var courseTime = ""
    var start = ""
    var courseId = ""
    private var lessonDays = mutableMapOf<Int, String>()
    private val selectedLessonDays = mutableMapOf<Int, Boolean>()

    private val safeArgs: AddGroupFragmentArgs by navArgs()

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAddGroupBinding.bind(view)
        bindingActBar = ActionBar2Binding.bind(view)
        val adapter2 = ArrayAdapter(requireContext(), R.layout.spinner_item, arrayListOf<String>())

        bindingActBar.actionBarTitle.text = view.context.getString(R.string.create_group)
        navController = Navigation.findNavController(view)

        setUpObserversGroup()
        viewModel.getAllTeachers()
        bindingActBar.apply {
            binding.apply {
                teachers.prompt = root.context!!.getString(R.string.selectTeacher)
                val sdf = SimpleDateFormat("dd.MM.yyyy")
                start = sdf.format(Calendar.getInstance().time).toString()
                startDate.text = start

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

                for(i in 1..7){
                    selectedLessonDays[i] = false
                }

                mon.onClick {
                    selected(it)
                    check()
                }
                tue.onClick {
                    selected(it)
                    check()

                }
                wed.onClick {
                    selected(it)
                    check()
                }
                thu.onClick {
                    selected(it)
                    check()
                }
                fri.onClick {
                    selected(it)
                    check()
                }
                sat.onClick {
                    selected(it)
                    check()
                }
                sun.onClick {
                    selected(it)
                    check()
                }
                startDate.onClick{
                    val dialog = CalendarDialog(requireContext())
                    dialog.show()
                    dialog.getData {data ->
                        start = data
                        startDate.text = start
                    }
                }

                btnSave.onClick {
                    var minut = timePicker.minute.toString()
                    var hour = timePicker.hour.toString()
                    if (timePicker.minute == 0)
                        minut = "${timePicker.minute}0"
                    if(timePicker.hour == 0)
                        hour = "${timePicker.hour}0"
                    if(timePicker.minute < 10)
                        minut = "0${timePicker.minute}"
                    if(timePicker.hour < 10)
                        hour = "0${timePicker.hour}"

                    courseTime = "$hour:$minut"

                    courseId = safeArgs.id
                    val name = groupName.text.toString()
                    if(name.isEmpty()) groupName.error = requireContext().getString(R.string.fillField)
                    if(dates.text.isNullOrEmpty()) dates.error = requireContext().getString(R.string.fillField)
                    if(name.isNotEmpty() && dates.text.isNotEmpty() && teacher.isNotEmpty()){
                        viewModel.createGroup(
                            name, teacher, courseId, courseTime, start, dates.text.toString())
                    }
                }
            }
            btnHome.onClick {
                navController.popBackStack()
            }
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
                        toastLNCenter(getString(R.string.added_succesfuly))
                        navController.popBackStack()
                    }
                    ResourceState.ERROR -> {
                        loading.visibility(false)
                        toastLN(it.message)
                    }
                }
            })
        }
    }
    private fun selected(view: View) {
        val v = (view as TextView).text.toString()
        val k = view.tag.toString().toInt()
        if(selectedLessonDays[k]!!){
            view.setBackgroundResource(0)
            lessonDays.remove(k)
            selectedLessonDays[k] = false
        }
        else {
            view.setBackgroundResource(R.drawable.selected)
            lessonDays[k] = v
            selectedLessonDays[k] = true
        }
        sort()
        binding.dates.visibility(true)
        val t = lessonDays.values.toString().substring(1, lessonDays.values.toString().length - 1)
        binding.dates.text = t
    }

    private fun check(){
        binding.apply {
            if(lessonDays.size == 7) {
                dates.text = getString(R.string.everyDay)
            }
            if(lessonDays.isEmpty()) {
                dates.text = ""
                dates.visibility(false)
            }
        }
    }
    private fun sort(){
       lessonDays = lessonDays.toSortedMap()
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        TODO("Not yet implemented")
    }
}

