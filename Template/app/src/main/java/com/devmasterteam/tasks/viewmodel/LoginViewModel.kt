package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.exception.NoInternetException
import com.devmasterteam.tasks.service.model.ValidationModel
import com.devmasterteam.tasks.service.repository.PersonRepositoty
import com.devmasterteam.tasks.service.repository.PriorityRepository
import com.devmasterteam.tasks.service.repository.local.PreferencesManager
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
import com.devmasterteam.tasks.view.LoginActivity
import com.google.gson.Gson
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : BaseAndroidViewModel(application) {
    private val preferencesManager = PreferencesManager(application.applicationContext)
    private val personRepositoty = PersonRepositoty(application.applicationContext)
    private val priorityRepository = PriorityRepository(application.applicationContext)

    private val _login = MutableLiveData<ValidationModel>()
    val login: LiveData<ValidationModel> = _login

    private val _loggedUser = MutableLiveData<Boolean>()
    val loggedUser: LiveData<Boolean> = _loggedUser

    fun login(email: String, password: String){
        viewModelScope.launch {
            try {
                val response = personRepositoty.login(email, password)
                if(response.isSuccessful && response.body() != null){
                    val personModel = response.body()!!

                    RetrofitClient.addHeaders(personModel.token, personModel.personKey)

                    super.saveUserAuthentication(personModel)
                    _login.value = ValidationModel()
                } else {
                    _login.value = errorMenssage(response)
                }
            } catch (e: NoInternetException){
                _login.value = ValidationModel(e.errorMessage)
            }
        }
    }

    fun verifyUserLogged(){
        viewModelScope.launch {
            val token = preferencesManager.get(TaskConstants.SHARED.TOKEN_KEY)
            val personKey = preferencesManager.get(TaskConstants.SHARED.PERSON_KEY)

            RetrofitClient.addHeaders(token, personKey)

            val logged = (token != "" && personKey != "")
            _loggedUser.value = logged

            if(!logged){
                val response = priorityRepository.getList()
                if(response.isSuccessful && response.body() != null){
                    priorityRepository.save(response.body()!!)
                }
            }
        }
    }
}