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
import kotlinx.coroutines.launch
import javax.inject.Inject

// TODO: Observe current task to update page with changes
@HiltViewModel
class CurrentTaskViewModel @Inject constructor(
    private val errorHolder: ErrorHolder, private val tasksRepository: TasksRepository) :
    ViewModel() {
    private val mCurrentTaskUIState: MutableLiveData<CurrentTaskUIState> by lazy {
        MutableLiveData<CurrentTaskUIState>()
    }

    val currentTaskUIState: LiveData<CurrentTaskUIState> = mCurrentTaskUIState

    fun setCurrentTask(task: Task, willAllowEdit: Boolean) {
        mCurrentTaskUIState.value = CurrentTaskUIState(task, willAllowEdit)
    }

    fun completeTask() {
        val currentTaskUIState = mCurrentTaskUIState.value!!

        viewModelScope.launch(Dispatchers.IO) {
            errorHolder.handleRepositoryResult(
                tasksRepository.completeTask(currentTaskUIState.task.copy())) { result ->
                mCurrentTaskUIState.value = CurrentTaskUIState(
                    result.content, currentTaskUIState.willShowEditButtons, result)
            }
        }
    }
}