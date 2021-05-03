package uz.texnopos.texnoposedufinance.ui.main.course.add

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import org.koin.android.viewmodel.ext.android.viewModel
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseFragment
import uz.texnopos.texnoposedufinance.core.ResourceState
import uz.texnopos.texnoposedufinance.core.extentions.enabled
import uz.texnopos.texnoposedufinance.core.extentions.onClick
import uz.texnopos.texnoposedufinance.core.extentions.visibility
import uz.texnopos.texnoposedufinance.databinding.ActionBarAddBinding
import uz.texnopos.texnoposedufinance.databinding.FragmentAddCourseBinding


class AddCourseFragment : BaseFragment(R.layout.fragment_add_course){

    private val viewModel: AddCourseViewModel by viewModel()
    lateinit var binding: FragmentAddCourseBinding
    lateinit var bindingActBar: ActionBarAddBinding
    lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAddCourseBinding.bind(view)
        bindingActBar = ActionBarAddBinding.bind(view)
        bindingActBar.actionBarTitle.text = view.context.getString(R.string.addCourse)

        navController = Navigation.findNavController(view)
        setUpObserversCourse()

        bindingActBar.btnHome.onClick {
            navController.popBackStack()
        }
        bindingActBar.apply {
            binding.apply {
                btnSave.onClick {
                    if (!name.text.isNullOrEmpty() &&
                        !price.text.isNullOrEmpty() && !duration.text.isNullOrEmpty()
                    ) {
                        val name = name.text.toString()
                        val price: Double = price.text.toString().toDouble()
                        val period = duration.text.toString().toInt()
                        viewModel.createCourse(name, period, price).toString()
                        isLoading(true)
                    } else {
                        if (name.text.isNullOrEmpty()) name.error =
                            view.context.getString(R.string.fillField)
                        if (price.text.isNullOrEmpty()) price.error =
                            view.context.getString(R.string.fillField)
                        if (duration.text.isNullOrEmpty()) duration.error =
                            view.context.getString(R.string.fillField)
                    }
                }
            }
        }

    }

    private fun setUpObserversCourse() {
        binding.apply{
            viewModel.createCourse.observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    ResourceState.LOADING -> {
                        isLoading(true)
                    }

                    ResourceState.SUCCESS -> {
                        isLoading(false)
                        toastLNCenter("Доваблен новый курс")
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
    private fun isLoading(b: Boolean){
        binding.apply {
            name.enabled(!b)
            price.enabled(!b)
            duration.enabled(!b)
            btnSave.enabled(!b)
            loading.visibility(b)
        }
    }
}
