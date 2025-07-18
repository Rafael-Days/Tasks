package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devmasterteam.tasks.service.model.ValidationModel
import com.devmasterteam.tasks.service.repository.PersonRepositoty
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
import kotlinx.coroutines.launch

class RegisterViewModel(application: Application) : BaseAndroidViewModel(application) {
    private val personRepositoty = PersonRepositoty(application.applicationContext)

    private val _create = MutableLiveData<ValidationModel>()
    val create: LiveData<ValidationModel> = _create

    fun create(name: String, email: String, password: String){
        viewModelScope.launch {
            try {
                val response = personRepositoty.create(name, email, password, "TRUE")
                if(response.isSuccessful && response.body() != null){
                    val personModel = response.body()!!
                    RetrofitClient.addHeaders(personModel.token, personModel.personKey)
                    super.saveUserAuthentication(personModel)
                    _create.value = ValidationModel()
                } else {
                    _create.value = parseErrorMenssage(response)
                }
            } catch (e:Exception){
                _create.value = handleException(e)
            }
        }
    }
}