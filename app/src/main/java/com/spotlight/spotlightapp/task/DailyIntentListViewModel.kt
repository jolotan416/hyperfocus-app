package com.spotlight.spotlightapp.task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spotlight.spotlightapp.data.SpotlightDatabaseHolder
import com.spotlight.spotlightapp.data.task.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DailyIntentListViewModel : ViewModel() {
    private val mutableTasks: MutableLiveData<List<Task>> by lazy {
        MutableLiveData<List<Task>>()
    }

    val tasks: LiveData<List<Task>> = mutableTasks

    fun requestTasks() {
        viewModelScope.launch(context = Dispatchers.IO) {
            val tasks = SpotlightDatabaseHolder.getInstance()
                .getTaskDao()
                .getDailyIntentList()

            withContext(Dispatchers.Main) {
                mutableTasks.value = tasks
            }
        }
    }
}