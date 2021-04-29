package uz.texnopos.texnoposedufinance.ui.main.group.add

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ScrollView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import org.koin.android.ext.android.inject
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment
import uz.texnopos.texnoposedufinance.core.ResourceState
import uz.texnopos.texnoposedufinance.core.extentions.enabled
import uz.texnopos.texnoposedufinance.core.extentions.onClick
import uz.texnopos.texnoposedufinance.core.extentions.visibility
import uz.texnopos.texnoposedufinance.databinding.ActionBar2Binding
import uz.texnopos.texnoposedufinance.databinding.FragmentAddGroupBinding
import java.text.SimpleDateFormat
import java.util.*

class AddGroupFragment : BaseFragment(R.layout.fragment_add_group){
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
    private val allTeachers = mutableListOf<String>()

    private val safeArgs: AddGroupFragmentArgs by navArgs()

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAddGroupBinding.bind(view)
        bindingActBar = ActionBar2Binding.bind(view)

        val teachersAdapter = ArrayAdapter(requireContext(), R.layout.item_spinner, allTeachers)

        bindingActBar.actionBarTitle.text = view.context.getString(R.string.create_group)
        navController = Navigation.findNavController(view)

        setUpObserversGroup()
        viewModel.getAllTeachers()
        bindingActBar.apply {
            binding.apply {
                val sdf = SimpleDateFormat("dd.MM.yyyy")
                start = sdf.format(Calendar.getInstance().time).toString()
                //start = LocalDateTime.now().toString()

                tvStart.text = start
                tpTime.setIs24HourView(true)
                viewModel.teacherList.observe(viewLifecycleOwner, Observer {
                    when (it.status) {
                        ResourceState.LOADING -> loading.visibility(true)
                        ResourceState.SUCCESS -> {
                            loading.visibility(false)
                            teachersAdapter.clear()
                            teachersAdapter.addAll(it.data!!.map { e -> e.name })
                        }
                        ResourceState.ERROR -> {
                            loading.visibility(false)
                            toastLN(it.message)
                        }
                    }
                })
                actTeachers.setAdapter(teachersAdapter)

                actTeachers.setOnItemClickListener { adapterView, _, i, _ ->
                    teacher = if (adapterView.getItemAtPosition(i).toString() != view.context.getString(R.string.doNotSelected)) {
                        viewModel.teacherList.value?.data!![i].name
                    } else ""
                }

                for (i in 1..7) {
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

                imgCalendar.onClick {
                    val dialog = CalendarDialog(requireContext())
                    dialog.show()
                    dialog.getData { data ->
                        start = data
                        tvStart.text = start
                    }
                }

                btnSave.onClick {
                    var min = tpTime.minute.toString()
                    var hour = tpTime.hour.toString()
                    if (tpTime.minute < 10)
                        min = "0${tpTime.minute}"
                    if (tpTime.hour < 10)
                        hour = "0${tpTime.hour}"

                    courseTime = "$hour:$min"
                    courseId = safeArgs.id
                    val name = groupName.text.toString()
                    val dates = tvDates.text.toString()
                    if (name.isEmpty()) groupName.error = view.context.getString(R.string.fillField)
                    if (dates.isEmpty()) toastLN(view.context.getString(R.string.daysNotSelected))
                    if(teacher == view.context.getString(R.string.doNotSelected) || teacher.isEmpty()) toastSHTop(view.context.getString(R.string.teachersNotSelected))
                    if (name.isNotEmpty() && tvDates.text.isNotEmpty() && teacher.isNotEmpty()) {
                        viewModel.createGroup(name, teacher, courseId, courseTime, start, dates)
                        isLoading(true)
                    }
                }

                btnHome.onClick {
                    navController.popBackStack()
                }
            }
        }
    }

    private fun setUpObserversGroup() {
        binding.apply {
            viewModel.createGroup.observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    ResourceState.LOADING -> {
                        isLoading(true)
                    }
                    ResourceState.SUCCESS -> {
                        isLoading(false)
                        toastLNCenter(getString(R.string.added_successfully))
                        navController.popBackStack()

                    }
                    ResourceState.ERROR -> {
                        isLoading(false)
                        toastLN(it.message)
                    }
                }
            })
        }
    }

    private fun selected(view: View) {
        val v = (view as TextView).text.toString()
        val k = view.tag.toString().toInt()
        if (selectedLessonDays[k]!!) {
            view.setBackgroundResource(0)
            lessonDays.remove(k)
            selectedLessonDays[k] = false
        } else {
            view.setBackgroundResource(R.drawable.selected)
            lessonDays[k] = v
            selectedLessonDays[k] = true
        }
        sort()
        binding.tvDates.visibility(true)
        val t = lessonDays.values.toString().substring(1, lessonDays.values.toString().length - 1)
        binding.tvDates.text = t
        binding.scrollView.fullScroll(ScrollView.FOCUS_DOWN)
    }

    private fun check() {
        binding.apply {
            if (lessonDays.size == 7) {
                tvDates.text = getString(R.string.everyDay)
            }
            if (lessonDays.isEmpty()) {
                tvDates.text = ""
                tvDates.visibility(false)
            }
        }
    }

    private fun sort() {
        lessonDays = lessonDays.toSortedMap()
    }
    private fun isLoading(b: Boolean){
        binding.apply {
            tvDates.enabled(!b)
            tvStart.enabled(!b)
            groupName.enabled(!b)
            actTeachers.enabled(!b)
            btnSave.enabled(!b)
            loading.visibility(b)
        }
    }

}



