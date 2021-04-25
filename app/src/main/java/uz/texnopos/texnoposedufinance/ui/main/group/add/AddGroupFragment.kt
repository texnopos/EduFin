package uz.texnopos.texnoposedufinance.ui.main.group.add

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
import uz.texnopos.texnoposedufinance.databinding.DialogCalendarBinding
import uz.texnopos.texnoposedufinance.databinding.FragmentAddGroupBinding


class AddGroupFragment: BaseFragment(R.layout.fragment_add_group), AdapterView.OnItemClickListener {
    private val viewModel: AddGroupViewModel by inject()
    private lateinit var binding: FragmentAddGroupBinding
    private lateinit var bindingActBar: ActionBar2Binding
    lateinit var navController: NavController
    var id = ""
    var teacher = ""
    var courseTime = ""
    var startDate = ""
    var courseId = ""
    private val lessonDays = mutableMapOf<Int, String>()


    private val safeArgs: AddGroupFragmentArgs by navArgs()

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

                mon.onClick {
                    val d = mon.text.toString()
//                    if(monSelected.visibility == View.GONE){
//                        monSelected.visibility(true)
//                        lessonDays[mon.tag] = d
//                    }
//                    else{
//                        monSelected.visibility(false)
//                        lessonDays.remove(1)
//                    }
                    date.text = lessonDays.toString()
                    check()
                }
                tue.onClick {
                    val d = tue.text.toString()
//                    if(tueSelected.visibility == View.GONE){
//                        tueSelected.visibility(true)
//                        lessonDays[2] = d
//                    }
//                    else{
//                        tueSelected.visibility(false)
//                        lessonDays.remove(2)
//                    }
                    date.text = lessonDays.toString()
                    check()
                }
                wed.onClick {
                    val d = wed.text.toString()
                    if(wedSelected.visibility == View.GONE){
                        wedSelected.visibility(true)
                        lessonDays[3] = d
                    }
                    else{
                        wedSelected.visibility(false)
                        lessonDays.remove(3)
                    }
                    date.text = lessonDays.toString()
                    check()
                }
                thu.onClick {
                    val d = tue.text.toString()
                    if(thuSelected.visibility == View.GONE){
                        thuSelected.visibility(true)
                        lessonDays[4] = d
                    }
                    else{
                        thuSelected.visibility(false)
                        lessonDays.remove(4)
                    }
                    date.text = lessonDays.toString()
                    check()
                }
                fri.onClick {
                    val d = fri.text.toString()
                    if(friSelected.visibility == View.GONE){
                        friSelected.visibility(true)
                        lessonDays[5] = d
                    }
                    else{
                        friSelected.visibility(false)
                        lessonDays.remove(5)
                    }
                    date.text = lessonDays.toString()
                    check()
                }

                sat.onClick {
                    val d = sat.text.toString()
                    if(satSelected.visibility == View.GONE){
                        satSelected.visibility(true)
                        lessonDays[6] = d
                    }
                    else{
                        satSelected.visibility(false)
                        lessonDays.remove(6)
                    }
                    date.text = lessonDays.toString()
                    check()
                }
                sun.onClick {
                    val d = sun.text.toString()
                    if(sunSelected.visibility == View.GONE){
                        sunSelected.visibility(true)
                        lessonDays[7] = d
                    }
                    else{
                        sunSelected.visibility(false)
                        lessonDays.remove(7)
                    }
                    date.text = lessonDays.toString()
                    check()
                }
                calendar.onClick{
                    val dialog = CalendarDialog(requireContext())
                    dialog.show()
                    dialog.getData {data ->
                        startDate = data
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
                    if(date.text.isNullOrEmpty()) date.error = requireContext().getString(R.string.fillField)
                    if(name.isNotEmpty() && date.text.isNotEmpty() && teacher.isNotEmpty()){
                        viewModel.createGroup(
                            name, teacher, courseId, courseTime, startDate, date.text.toString())
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

    private fun check(){
        binding.apply {
            if(lessonDays.size == 7) {
                date.text = getString(R.string.everyDay)
            }
            if(lessonDays.isEmpty()) date.text = ""
        }

    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        TODO("Not yet implemented")
    }
}

