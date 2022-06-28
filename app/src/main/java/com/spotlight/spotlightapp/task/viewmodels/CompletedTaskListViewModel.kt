package com.spotlight.spotlightapp.task.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.spotlight.spotlightapp.data.task.Task
import com.spotlight.spotlightapp.task.repo.TasksRepository
import com.spotlight.spotlightapp.utilities.viewmodelutils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CompletedTaskListViewModel @Inject constructor(tasksRepository: TasksRepository)
    : BaseViewModel() {
    val completedTaskList: LiveData<List<Task>> = tasksRepository.completedTasks
        .asLiveData()
}