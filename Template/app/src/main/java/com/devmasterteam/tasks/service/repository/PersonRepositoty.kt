package com.devmasterteam.tasks.service.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.telecom.ConnectionService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.service.exception.NoInternetException
import com.devmasterteam.tasks.service.model.PersonModel
import com.devmasterteam.tasks.service.model.ValidationModel
import com.devmasterteam.tasks.service.repository.remote.PersonService
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
import retrofit2.Response

class PersonRepositoty(val context: Context) :BaseRepository(context){

    private val remote = RetrofitClient.getService(PersonService::class.java)

    suspend fun login(email: String, password: String): Response<PersonModel> {

        return saveAPICall { remote.login(email, password) }
    }

    suspend fun create(
        name: String,
        email: String,
        password: String,
        receiveNews: String
    ): Response<PersonModel> {

        return saveAPICall { remote.create(name, email, password, receiveNews) }
    }

}

//activity viewmodel repositorio
//, receivenews: String