package com.spotlight.spotlightapp.task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.spotlight.spotlightapp.data.task.Task

class CurrentTaskViewModel : ViewModel() {
    private val mCurrentTask: MutableLiveData<Task> by lazy {
        MutableLiveData<Task>()
    }

    val currentTask: LiveData<Task> = mCurrentTask

    fun setCurrentTask(task: Task) {
        mCurrentTask.value = task
    }
}