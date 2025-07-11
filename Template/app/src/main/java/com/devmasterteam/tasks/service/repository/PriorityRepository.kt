package com.devmasterteam.tasks.service.repository

import android.content.Context
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.repository.local.TaskDatabase
import com.devmasterteam.tasks.service.repository.remote.PriorityService
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class PriorityRepository(context: Context) {

    private val remote = RetrofitClient.getService(PriorityService::class.java)
    private val database = TaskDatabase.getDatabase(context).priorityDAO()

    suspend fun getList(): Response<List<PriorityModel>>{
        return remote.getList()
    }

    fun list(): Flow<List<PriorityModel>> {
        return database.list()
    }

    suspend fun getDescription(id: Int): String {
        return database.getDescription(id)
    }

    suspend fun save(list: List<PriorityModel>){
        database.clear()
        database.save(list)
    }

}