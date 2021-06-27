package uz.texnopos.texnoposedufinance.ui.main.info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.texnopos.texnoposedufinance.core.Resource
import uz.texnopos.texnoposedufinance.data.firebase.InfoHelper
import uz.texnopos.texnoposedufinance.data.model.Organization
import uz.texnopos.texnoposedufinance.data.model.Teacher

class InfoViewModel(private val helper: InfoHelper): ViewModel() {
    private val _org: MutableLiveData<Resource<Organization>> = MutableLiveData()
    val org: LiveData<Resource<Organization>>
        get() = _org

    fun getOrgData(){
        _org.value = Resource.loading()
        helper.getOrgData({
            _org.value = Resource.success(it)
        }, {
            _org.value = Resource.error(it)
        })
    }

    private val _orgUpdate: MutableLiveData<Resource<Organization>> = MutableLiveData()
    val orgUpdate: LiveData<Resource<Organization>>
        get() = _orgUpdate

    fun updateOrgData(name: String,  address: String, score: String, mfo: String,
                      phone: String, inn: String, director: String, bank: String){
        _orgUpdate.value = Resource.loading()
        helper.updateOrgData(name, address, score, mfo, phone, inn, director, bank, {
            _orgUpdate.value = Resource.success(Organization())
        }, {
            _orgUpdate.value = Resource.error(it)
        })
    }

}