package com.devmasterteam.tasks.service.model

import com.google.gson.annotations.SerializedName

data class TaskModel (

    @SerializedName("Id")
    val id: Int,

    @SerializedName("PriorityId")
    val priorityId: Int,

    @SerializedName("Description")
    val description: String,

    @SerializedName("DueDate")
    val dueDate: String,

    @SerializedName("Complete")
    val complete: Boolean,

    var priorityDescription: String = ""
)