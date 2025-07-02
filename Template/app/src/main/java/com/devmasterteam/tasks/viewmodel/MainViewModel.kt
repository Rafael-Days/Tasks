package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.repository.local.PreferencesManager
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val preferencesManager = PreferencesManager(application.applicationContext)

    fun loggout(){
        viewModelScope.launch {
            preferencesManager.remove(TaskConstants.SHARED.TOKEN_KEY)
            preferencesManager.remove(TaskConstants.SHARED.PERSON_KEY)
            preferencesManager.remove(TaskConstants.SHARED.PERSON_NAME)
        }
    }
}