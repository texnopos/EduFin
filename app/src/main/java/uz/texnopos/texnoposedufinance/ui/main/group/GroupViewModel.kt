package uz.texnopos.texnoposedufinance.ui.main.group

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.texnopos.texnoposedufinance.core.Resource
import uz.texnopos.texnoposedufinance.data.firebase.GroupHelper
import uz.texnopos.texnoposedufinance.data.model.Group

class GroupViewModel(private val helper: GroupHelper): ViewModel() {
    private val _groupList: MutableLiveData<Resource<List<Group>>> = MutableLiveData()
    val groupList: LiveData<Resource<List<Group>>>
        get() = _groupList
    fun getAllGroups(courseId: String){
        _groupList.value = Resource.loading()
        helper.getAllGroups(courseId,
            { _groupList.value = Resource.success(it)
            }, {
                _groupList.value = Resource.error(it)
            })
    }
}