package uz.texnopos.texnoposedufinance.ui.main.teacher

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
import uz.texnopos.texnoposedufinance.databinding.ActionBarBinding
import uz.texnopos.texnoposedufinance.databinding.AddActionBarBinding
import uz.texnopos.texnoposedufinance.databinding.FragmentTeachersInfoBinding

class TeachersInfoFragment: BaseFragment(R.layout.fragment_teachers_info) {
    lateinit var binding: FragmentTeachersInfoBinding
    lateinit var actBinding: AddActionBarBinding
    private val arg: TeachersInfoFragmentArgs by navArgs()
    lateinit var navController: NavController
    private val viewModel: TeacherViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentTeachersInfoBinding.bind(view)
        actBinding = AddActionBarBinding.bind(view)
        navController = Navigation.findNavController(view)

        val teacherId = arg.teacherId
        setUpObserversDelete()

        actBinding.apply {
            actionBarTitle.text = view.context.getString(R.string.teachers_info)

            btnHome.onClick {
                navController.popBackStack()
            }
            binding.apply {
                delete.onClick {
                    viewModel.deleteTeacher(teacherId)
                }
            }
        }

    }
    private fun setUpObserversDelete(){
        binding.apply {
            viewModel.deleted.observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    ResourceState.LOADING -> loading.visibility(true)
                    ResourceState.SUCCESS -> {
                        loading.visibility(false)
                        toastLN("Успешно удален")
                    }
                    ResourceState.ERROR -> {
                        loading.visibility(false)
                        toastLN(it.message)
                    }
                    else -> {
                    }
                }
            })
        }
    }
}