package uz.texnopos.texnoposedufinance.ui.main.group

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.texnopos.texnoposedufinance.core.Resource
import uz.texnopos.texnoposedufinance.data.firebase.GroupHelper
import uz.texnopos.texnoposedufinance.data.model.Group

class GroupViewModel(private val helper: GroupHelper): ViewModel() {
    private val _group: MutableLiveData<Resource<Group>> = MutableLiveData()
    val group:LiveData<Resource<Group>>
        get() = _group
    fun getDatCurrentGroup(groupId: String){
        _group.value = Resource.loading()
        helper.getDataCurrentGroup(groupId, {
            _group.value = Resource.success(Group())
        }, {
            _group.value = Resource.error(it)
        })
    }
}