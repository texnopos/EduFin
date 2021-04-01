package uz.texnopos.texnoposedufinance.ui.main.group

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.texnopos.texnoposedufinance.core.Resource
import uz.texnopos.texnoposedufinance.data.firebase.GroupHelper
import uz.texnopos.texnoposedufinance.data.model.Group

class GroupViewModel(private val helper: GroupHelper): ViewModel() {
    private val _groupResult: MutableLiveData<Resource<List<Group>>> = MutableLiveData()
    val groupResult: LiveData<Resource<List<Group>>>
        get() = _groupResult

    fun getAllGroups(courseId: String){
        _groupResult.value = Resource.loading()
        helper.getAllGroups(
            {
                _groupResult.value = Resource.success(it)
            }, {
                _groupResult.value = Resource.error(it)
            })
    }
}