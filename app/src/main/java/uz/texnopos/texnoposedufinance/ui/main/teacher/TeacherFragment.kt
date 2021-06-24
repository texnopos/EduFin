package uz.texnopos.texnoposedufinance.ui.main.teacher

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import uz.texnopos.texnoposedufinance.MainActivity
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment
import uz.texnopos.texnoposedufinance.core.RealtimeChangesResourceState
import uz.texnopos.texnoposedufinance.core.ResourceState
import uz.texnopos.texnoposedufinance.core.extentions.visibility
import uz.texnopos.texnoposedufinance.databinding.ActionBarBinding
import uz.texnopos.texnoposedufinance.databinding.FragmentTeachersBinding
import uz.texnopos.texnoposedufinance.ui.main.MainFragmentDirections


class TeacherFragment : BaseFragment(R.layout.fragment_teachers) {

    private val adapter: TeacherAdapter by inject()
    private val viewModel: TeacherViewModel by viewModel()

    private lateinit var navController: NavController
    private lateinit var binding: FragmentTeachersBinding
    private lateinit var bindingActionBar: ActionBarBinding
    lateinit var parentNavController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentTeachersBinding.bind(view)
        bindingActionBar = ActionBarBinding.bind(view)
        bindingActionBar.tvTitle.text = view.context.getString(R.string.teachers)
        navController = Navigation.findNavController(view)
        setUpObservers()
        binding.apply {
            rcvTeachers.adapter = adapter
        }
        if (requireParentFragment().requireActivity() is MainActivity) {
            parentNavController = Navigation.findNavController(requireParentFragment().requireActivity() as MainActivity, R.id.nav_host)
        }
        adapter.setOnItemClicked {
            val action = MainFragmentDirections.actionMainFragmentToTeachersInfoFragment(it)
            parentNavController.navigate(action)
        }
        viewModel.getAllTeachers()
    }

    private fun setUpObservers() {
        adapter.models = mutableListOf()
        binding.apply {
            viewModel.teacherList.observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    RealtimeChangesResourceState.LOADING -> {
                        loading.visibility(true)
                    }
                    RealtimeChangesResourceState.ADDED -> {
                        loading.visibility(false)
                        adapter.onAdded(it.data!!)
                    }
                    RealtimeChangesResourceState.MODIFIED -> {
                        loading.visibility(false)
                        adapter.onModified(it.data!!)
                    }
                    RealtimeChangesResourceState.REMOVED -> {
                        loading.visibility(false)
                        adapter.onRemoved(it.data!!)
                    }
                    RealtimeChangesResourceState.ERROR -> {
                        toastLN(it.message)
                        loading.visibility(false)
                    }
                }
            })
        }
    }
    private fun isLoading(b: Boolean){
        binding.apply {
            loading.visibility(b)
            rcvTeachers.visibility(!b)
        }
    }
}
/*private fun optionsClicked(view: View){
    val menu = PopupMenu(requireContext(), view)
    val menuInflater = menu.menuInflater
    menuInflater.inflate(R.menu.teacher_menu, menu.menu)
    menu.show()
    menu.setOnMenuItemClickListener {id ->
        binding.apply {
            when(id.itemId){
                R.id.delete_teacher ->{

                }
            }
        }
        return@setOnMenuItemClickListener true
    }
}*/
