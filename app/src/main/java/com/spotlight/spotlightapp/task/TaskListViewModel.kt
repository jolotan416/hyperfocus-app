package com.spotlight.spotlightapp.task

import androidx.lifecycle.*
import com.spotlight.spotlightapp.data.SpotlightDatabaseHolder
import com.spotlight.spotlightapp.data.task.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// TODO: Add save state to ViewModel
class TaskListViewModel : ViewModel() {
    private val mutableTasks: MutableLiveData<List<Task>> by lazy {
        MutableLiveData<List<Task>>()
    }

    val dailyIntentList = MediatorLiveData<List<Task>>().apply {
        addSource(mutableTasks) { tasks ->
            value = tasks.filter { it.priority != 0 }
                .sortedBy { it.priority }
        }
    }

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