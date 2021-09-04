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

// TODO: This should probably be converted to a shared ViewModel between task fragments
// TODO: Add save state to ViewModel
class DailyIntentListViewModel : ViewModel() {
    private val mutableTasks: MutableLiveData<List<Task>> by lazy {
        MutableLiveData<List<Task>>()
    }

    private val mutableWillShowEmptyState: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val tasks: LiveData<List<Task>> = mutableTasks
    val willShowEmptyState: LiveData<Boolean> = mutableWillShowEmptyState

    fun requestTasks() {
        viewModelScope.launch(context = Dispatchers.IO) {
            // TODO: Transfer to Repository
            val tasks = SpotlightDatabaseHolder.getInstance()
                .getTaskDao()
                .getDailyIntentList()

            withContext(Dispatchers.Main) {
                if (tasks.isNotEmpty()) {
                    mutableTasks.value = tasks
                }

                mutableWillShowEmptyState.value = tasks.isEmpty()
            }
        }
    }
}