package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.service.model.ValidationModel
import com.devmasterteam.tasks.service.repository.PriorityRepository
import com.devmasterteam.tasks.service.repository.TaskRepository
import kotlinx.coroutines.launch

class TaskListViewModel(application: Application) : BaseAndroidViewModel(application) {

    private val taskRepository = TaskRepository(application.applicationContext)
    private val priorityRepository = PriorityRepository(application.applicationContext)

    private val _tasks = MutableLiveData<List<TaskModel>>()
    val tasks: LiveData<List<TaskModel>> = _tasks

    private val _taskDeleted = MutableLiveData<ValidationModel>()
    val taskDeleted: LiveData<ValidationModel> = _taskDeleted

    private var taskFilter = TaskConstants.FILTER.ALL

    fun list(filter: Int){
        taskFilter = filter
        viewModelScope.launch {
            val response = when (filter){
                TaskConstants.FILTER.ALL -> taskRepository.list()
                TaskConstants.FILTER.NEXT -> taskRepository.listNext()
                else -> taskRepository.listOverdue()
            }

            if(response.isSuccessful && response.body() != null){
                val result = response.body()!!

                result.map{ task ->
                    task.priorityDescription = priorityRepository.getDescription(task.priorityId)
                }

                _tasks.value = result
            }
        }
    }

    fun delete(taskId: Int){
        viewModelScope.launch {
            try {
                val response = taskRepository.delete(taskId)
                if(response.isSuccessful && response.body() != null){
                    list(taskFilter)
                    _taskDeleted.value = ValidationModel()
                }
            }catch (e:Exception){
                _taskDeleted.value = handleException(e)
            }
        }
    }

    fun status(taskId: Int, complete: Boolean){
        viewModelScope.launch {
            val response = if(complete){
                taskRepository.complete(taskId)
            } else{
                taskRepository.undo(taskId)
            }

            if(response.isSuccessful && response.body() != null){
                list(taskFilter)
            }
        }
    }
}