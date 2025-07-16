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

    companion object{
        private val cache = mutableMapOf<Int, String>()

        fun setCacheDescription(id: Int, value: String){
            cache[id] = value
        }

        fun getCacheDescription(id: Int) = cache[id] ?: ""
    }

    suspend fun getList(): Response<List<PriorityModel>>{
        return remote.getList()
    }

    fun list(): Flow<List<PriorityModel>> {
        return database.list()
    }

    suspend fun getDescription(id: Int): String {
        val cached = getCacheDescription(id)
        return if(cached == ""){
            val description = database.getDescription(id)
            setCacheDescription(id, description)
             description
        }else{
             cached
        }
    }

    suspend fun save(list: List<PriorityModel>){
        database.clear()
        database.save(list)
    }

}