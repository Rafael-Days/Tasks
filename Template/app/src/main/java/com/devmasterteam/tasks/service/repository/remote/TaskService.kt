package com.devmasterteam.tasks.service.repository.remote

import android.app.ActivityManager.TaskDescription
import com.devmasterteam.tasks.service.model.TaskModel
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TaskService {

    @GET("Task")
    suspend fun list(): Response<List<TaskModel>>

    @GET("Task/Next7Days")
    suspend fun listNext(): Response<List<TaskModel>>

    @GET("Task/Overdue")
    suspend fun listOverdue(): Response<List<TaskModel>>

    @GET("Task/{id}")
    suspend fun load(
        @Path(value = "id", encoded = true) id: Int
    ): Response<TaskModel>

    @POST("Task")
    @FormUrlEncoded
    suspend fun create(
        @Field("PriorityId") priorityId: Int,
        @Field("Description") description: String,
        @Field("DueDate") dueDate: String,
        @Field("Complete") complete: Boolean
    ): Response<Boolean>

    @PUT("Task")
    @FormUrlEncoded
    suspend fun update(
        @Field("Id") id: Int,
        @Field("PriorityId") priorityId: Int,
        @Field("Description") description: String,
        @Field("DueDate") dueDate: Int,
        @Field("Complete") complete: Boolean
    ): Response<Boolean>

    @PUT("Task/Complete")
    @FormUrlEncoded
    suspend fun complete(@Field("Id") id: Int): Response<Boolean>

    @PUT("Task/Undo")
    @FormUrlEncoded
    suspend fun undo(@Field("Id") id: Int): Response<Boolean>

    @HTTP(method = "DELETE", path = "Task", hasBody = true)
    @FormUrlEncoded
    suspend fun delete(@Field("Id") id: Int): Response<Boolean>
}