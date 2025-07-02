package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.model.ValidationModel
import com.devmasterteam.tasks.service.repository.PersonRepositoty
import com.devmasterteam.tasks.service.repository.local.PreferencesManager
import com.google.gson.Gson
import kotlinx.coroutines.launch

class RegisterViewModel(application: Application) : BaseAndroidViewModel(application) {

    private val personRepositoty = PersonRepositoty()

    private val _create = MutableLiveData<ValidationModel>()
    val create: LiveData<ValidationModel> = _create

    fun create(name: String, email: String, password: String){
        viewModelScope.launch {
            val response = personRepositoty.create(name, email, password, "TRUE")
            if(response.isSuccessful && response.body() != null){
                super.saveUserAuthentication(response.body()!!)
                _create.value = ValidationModel()
            } else {
                _create.value = errorMenssage(response)
            }
        }
    }
}