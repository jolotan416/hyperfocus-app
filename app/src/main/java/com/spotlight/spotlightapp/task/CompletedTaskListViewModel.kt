package com.spotlight.spotlightapp.task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spotlight.spotlightapp.data.task.Task
import com.spotlight.spotlightapp.task.repo.TasksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CompletedTaskListViewModel @Inject constructor(private val tasksRepository: TasksRepository)
    : ViewModel() {
    init {
        viewModelScope.launch(Dispatchers.IO) {
            tasksRepository.completedTasks.collect { tasks ->
                withContext(Dispatchers.Main) {
                    mCompletedTaskList.value = tasks
                }
            }
        }
    }

    private val mCompletedTaskList: MutableLiveData<List<Task>> by lazy {
        MutableLiveData<List<Task>>()
    }

    val completedTaskList: LiveData<List<Task>> = mCompletedTaskList
}