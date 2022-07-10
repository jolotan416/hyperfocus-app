package com.spotlight.spotlightapp.task.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spotlight.spotlightapp.data.task.Task
import com.spotlight.spotlightapp.task.repo.TasksRepository
import com.spotlight.spotlightapp.task.viewdata.CurrentTaskUIState
import com.spotlight.spotlightapp.utilities.viewmodelutils.ErrorHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CurrentTaskViewModel @Inject constructor(
    private val errorHolder: ErrorHolder, private val tasksRepository: TasksRepository) :
    ViewModel() {
    private var taskFlowObserverJob: Job? = null

    private val mCurrentTaskUIState: MutableLiveData<CurrentTaskUIState> by lazy {
        MutableLiveData<CurrentTaskUIState>()
    }

    val currentTaskUIState: LiveData<CurrentTaskUIState> = mCurrentTaskUIState

    fun setCurrentTask(currentTask: Task, willAllowEdit: Boolean) {
        mCurrentTaskUIState.value = CurrentTaskUIState(currentTask, willAllowEdit)

        taskFlowObserverJob?.cancel()
        taskFlowObserverJob = viewModelScope.launch(Dispatchers.IO) {
            tasksRepository.observeTask(currentTask.id).collect { task ->
                withContext(Dispatchers.Main) {
                    mCurrentTaskUIState.value = currentTaskUIState.value!!.copy(
                        task = task ?: return@withContext)
                }
            }
        }
    }

    fun deleteTask() {
        val task = currentTaskUIState.value!!.task
        viewModelScope.launch(Dispatchers.IO) {
            errorHolder.handleRepositoryResult(tasksRepository.deleteTask(task.copy())) { result ->
                mCurrentTaskUIState.value = currentTaskUIState.value!!.copy(
                    deleteTaskResult = result)
            }
        }
    }

    fun toggleTaskFinished() {
        viewModelScope.launch(Dispatchers.IO) {
            errorHolder.handleRepositoryResult(
                tasksRepository.toggleTaskFinished(
                    currentTaskUIState.value!!.task.copy())) { result ->
                mCurrentTaskUIState.value = currentTaskUIState.value!!.copy(
                    task = result.content, completeTaskResult = result)
            }
        }
    }
}