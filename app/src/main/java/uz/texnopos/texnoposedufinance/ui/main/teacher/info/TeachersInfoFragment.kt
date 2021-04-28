package uz.texnopos.texnoposedufinance.ui.main.teacher.info

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import org.koin.android.viewmodel.ext.android.viewModel
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment
import uz.texnopos.texnoposedufinance.core.ResourceState
import uz.texnopos.texnoposedufinance.core.extentions.onClick
import uz.texnopos.texnoposedufinance.core.extentions.visibility
import uz.texnopos.texnoposedufinance.databinding.FragmentTeachersInfoBinding
import uz.texnopos.texnoposedufinance.databinding.InfoActionBarBinding
import uz.texnopos.texnoposedufinance.ui.main.teacher.TeacherViewModel
import uz.texnopos.texnoposedufinance.ui.main.teacher.info.TeachersInfoFragmentArgs

class TeachersInfoFragment : BaseFragment(R.layout.fragment_teachers_info) {
    lateinit var binding: FragmentTeachersInfoBinding
    lateinit var actBinding: InfoActionBarBinding
    private val arg: TeachersInfoFragmentArgs by navArgs()
    lateinit var navController: NavController
    private val viewModel: TeacherViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentTeachersInfoBinding.bind(view)
        actBinding = InfoActionBarBinding.bind(view)
        navController = Navigation.findNavController(view)

        val teacherId = arg.teacherId
        setUpObserversDelete()
        //setUpObservers()
        //viewModel.getDataCurrentTeacher(teacherId)

        actBinding.apply {
            actionBarTitle.text = view.context.getString(R.string.teachers_info)

            btnHome.onClick {
                navController.popBackStack()
            }
            binding.apply {
                delete.onClick {
                    val dialog = AlertDialog.Builder(requireContext())
                    dialog.setTitle("Удалить")
                    dialog.setMessage("Вы точно хотите удалить?")
                    dialog.setIcon(R.drawable.ic_warning)
                    dialog.setPositiveButton("Да") { _, _ ->
                        viewModel.deleteTeacher(teacherId)
                        navController.popBackStack()
                    }
                    dialog.setNegativeButton("Отмена") { dialog, _ ->
                        dialog.dismiss()
                    }
                    dialog.show()
                }

            }
        }
    }


    private fun setUpObserversDelete() {
        binding.apply {
            viewModel.deleted.observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    ResourceState.LOADING -> loading.visibility(true)
                    ResourceState.SUCCESS -> {
                        loading.visibility(false)
                        toastLN("Успешно удалено")
                    }
                    ResourceState.ERROR -> {
                        loading.visibility(false)
                        toastLN(it.message)
                    }
                }
            })
        }
    }

    private fun setUpObservers() {
        binding.apply {
            viewModel.current.observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    ResourceState.LOADING -> loading.visibility(true)
                    ResourceState.SUCCESS -> {
                        loading.visibility(false)
                    }
                    ResourceState.ERROR -> {
                        toastLN(it.message)
                        loading.visibility(false)
                    }
                }
            })
        }
    }
}