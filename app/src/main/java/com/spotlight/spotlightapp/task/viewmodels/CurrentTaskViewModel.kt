package com.spotlight.spotlightapp.task.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.spotlight.spotlightapp.data.task.Task
import com.spotlight.spotlightapp.task.repo.TasksRepository
import com.spotlight.spotlightapp.task.viewdata.CurrentTaskUIState
import com.spotlight.spotlightapp.utilities.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrentTaskViewModel @Inject constructor(private val tasksRepository: TasksRepository) :
    BaseViewModel() {
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
            handleRepositoryResult(
                tasksRepository.completeTask(currentTaskUIState.task.copy())) { result ->
                mCurrentTaskUIState.value = CurrentTaskUIState(
                    result.content, currentTaskUIState.willShowEditButtons, result)
            }
        }
    }
}