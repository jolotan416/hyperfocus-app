package com.spotlight.spotlightapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spotlight.spotlightapp.data.task.Task
import com.spotlight.spotlightapp.task.repo.TasksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val tasksRepository: TasksRepository) :
    ViewModel() {
    private val mCurrentRunningTask: MutableLiveData<Task?> by lazy {
        MutableLiveData<Task?>(null)
    }

    val currentRunningTask: LiveData<Task?> = mCurrentRunningTask

    fun checkForRunningTask() {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                mCurrentRunningTask.value = tasksRepository.dailyIntentList
                    .first()
                    .firstOrNull { it.taskTimerData != null }
            }
        }
    }

    fun notifyCurrentRunningTaskHandled() {
        mCurrentRunningTask.value = null
    }
}