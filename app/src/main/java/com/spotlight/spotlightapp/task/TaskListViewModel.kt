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

// TODO: Add save state to ViewModel
class TaskListViewModel : ViewModel() {
    private val mutableDailyIntentList: MutableLiveData<List<Task>> by lazy {
        MutableLiveData<List<Task>>()
    }

    private val mutablePendingTaskList: MutableLiveData<List<Task>> by lazy {
        MutableLiveData<List<Task>>()
    }

    val dailyIntentList: LiveData<List<Task>> = mutableDailyIntentList

    val pendingTaskList: LiveData<List<Task>> = mutablePendingTaskList

    fun requestDailyIntentList() {
        viewModelScope.launch(context = Dispatchers.IO) {
            val dailyIntentList = SpotlightDatabaseHolder.getInstance()
                .getTaskDao()
                .getDailyIntentList()

            withContext(Dispatchers.Main) {
                mutableDailyIntentList.value = dailyIntentList
            }
        }
    }

    fun requestPendingTasks() {
        viewModelScope.launch(context = Dispatchers.IO) {
            val pendingTasks = SpotlightDatabaseHolder.getInstance()
                .getTaskDao()
                .getTasks(false)

            withContext(Dispatchers.Main) {
                mutablePendingTaskList.value = sortTasks(pendingTasks)
            }
        }
    }

    private fun sortTasks(tasks: List<Task>) =
        tasks.filter { it.priority != 0 }.sortedBy { it.priority } +
                tasks.filter { it.priority == 0 }
}