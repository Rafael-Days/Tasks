package com.devmasterteam.tasks.service.repository

import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
import com.devmasterteam.tasks.service.repository.remote.TaskService
import retrofit2.Response

class TaskRepository {

    private val remote = RetrofitClient.getService(TaskService::class.java)

    suspend fun save(task: TaskModel){
        remote.create(task.priorityId, task.description, task.dueDate, task.complete)
    }

    suspend fun list(): Response<List<TaskModel>> {
        return remote.list()
    }

    suspend fun listNext(): Response<List<TaskModel>> {
        return remote.listNext()
    }

    suspend fun listOverdue(): Response<List<TaskModel>> {
        return remote.listOverdue()
    }
}