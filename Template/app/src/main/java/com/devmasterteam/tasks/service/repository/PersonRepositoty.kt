package com.devmasterteam.tasks.service.repository

import com.devmasterteam.tasks.service.model.PersonModel
import com.devmasterteam.tasks.service.repository.remote.PersonService
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
import retrofit2.Response

class PersonRepositoty {

    private val remote = RetrofitClient.getService(PersonService::class.java)

    suspend fun login(email: String, password: String): Response<PersonModel>{
        return remote.login(email,password)
    }

    suspend fun create(name: String, email: String, password: String, receiveNews: String): Response<PersonModel>{
        return remote.create(name, email, password, receiveNews)
    }
}

//activity viewmodel repositorio
//, receivenews: String