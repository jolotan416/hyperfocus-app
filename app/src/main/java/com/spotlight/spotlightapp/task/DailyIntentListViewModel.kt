package com.spotlight.spotlightapp.task

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.spotlight.spotlightapp.data.task.Task
import com.spotlight.spotlightapp.task.repo.TasksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DailyIntentListViewModel @Inject constructor(private val tasksRepository: TasksRepository)
    : ViewModel() {
    val dailyIntentList: LiveData<List<Task>>
        get() = tasksRepository.dailyIntentList.asLiveData()
}