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
import uz.texnopos.texnoposedufinance.databinding.FragmentTeacherInfoBinding
import uz.texnopos.texnoposedufinance.databinding.InfoActionBarBinding
import uz.texnopos.texnoposedufinance.ui.main.teacher.TeacherViewModel

class TeacherInfoFragment : BaseFragment(R.layout.fragment_teacher_info) {
    lateinit var binding: FragmentTeacherInfoBinding
    lateinit var actBinding: InfoActionBarBinding
    private val arg: TeacherInfoFragmentArgs by navArgs()
    lateinit var navController: NavController
    private val viewModel: TeacherViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentTeacherInfoBinding.bind(view)
        actBinding = InfoActionBarBinding.bind(view)
        navController = Navigation.findNavController(view)

        val teacherId = arg.teacherId
        setUpObserversDelete()
        setUpObservers()
        setUpObserversUpdate()
        viewModel.getDataCurrentTeacher(teacherId)
        isLoading(true)

        actBinding.apply {
            actionBarTitle.text = view.context.getString(R.string.teachers_info)

            btnHome.onClick {
                navController.popBackStack()
            }

            edit.onClick {
                //
            }
            binding.apply {
                delete.onClick {
                    val dialog = AlertDialog.Builder(requireContext())
                    dialog.setTitle("Удалить")
                    dialog.setMessage("Вы точно хотите удалить?")
                    dialog.setIcon(R.drawable.ic_warning)
                    dialog.setPositiveButton("Да") { _, _ ->
                        viewModel.deleteTeacher(teacherId)
                        isLoading(true)
                        navController.popBackStack()
                    }
                    dialog.setNegativeButton("Отмена") { dialog, _ ->
                        dialog.dismiss()
                    }
                    dialog.show()
                }

                btnSave.onClick {
                    viewModel.updateDataCurrentTeacher(teacherId, etName.text.toString(),
                        etPhone.text.toString(), etUsername.text.toString(), etSalary.text.toString())
                    isLoading(true)
                    navController.popBackStack()
                }

            }
        }
    }

    private fun setUpObserversUpdate() {
        binding.apply {
            viewModel.updateTeacher.observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    ResourceState.LOADING -> {
                        loading.visibility(true)
                    }
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

    private fun setUpObserversDelete() {
        binding.apply {
            viewModel.deleted.observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    ResourceState.LOADING -> isLoading(true)
                    ResourceState.SUCCESS -> {
                        isLoading(false)
                        toastLN("Успешно удалено")
                    }
                    ResourceState.ERROR -> {
                        isLoading(false)
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
                    ResourceState.LOADING -> isLoading(true)
                    ResourceState.SUCCESS -> {
                        etName.setText(it.data!!.name)
                        etUsername.setText(it.data.username)
                        etSalary.setText(it.data.salary)
                        etPhone.setText(it.data.phone)
                        isLoading(false)
                    }
                    ResourceState.ERROR -> {
                        toastLN(it.message)
                        isLoading(false)
                    }
                }
            })
        }
    }
    private fun isLoading(b: Boolean){
        binding.apply {
            btnSave.isEnabled = !b
            etName.isEnabled = !b
            etPhone.isEnabled = !b
            etSalary.isEnabled = !b
            etUsername.isEnabled = !b
            loading.isEnabled = !b
        }
    }
    private fun edited(b: Boolean){
        binding.apply {
            btnSave.isEnabled = b
            etName.isEnabled = b
            etPhone.isEnabled = b
            etSalary.isEnabled = b
            etUsername.isEnabled = b
        }
    }
}

