package com.spotlight.spotlightapp.task

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.spotlight.spotlightapp.data.task.Task
import com.spotlight.spotlightapp.task.repo.TasksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

// TODO: Add save state to ViewModel
@HiltViewModel
class PendingTaskListViewModel @Inject constructor(private val tasksRepository: TasksRepository)
    : ViewModel() {
    val pendingTaskList: LiveData<List<Task>>
        get() = tasksRepository.pendingTasks.asLiveData()
}