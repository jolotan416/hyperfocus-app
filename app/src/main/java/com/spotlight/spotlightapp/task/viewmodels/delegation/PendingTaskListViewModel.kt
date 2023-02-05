package com.spotlight.spotlightapp.task.viewmodels.delegation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.spotlight.spotlightapp.data.task.Task
import com.spotlight.spotlightapp.task.repo.TasksRepository
import com.spotlight.spotlightapp.utilities.viewmodelutils.ErrorHolderDelegate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PendingTaskListViewModel @Inject constructor(
    private val errorHolderDelegate: ErrorHolderDelegate,
    private val tasksRepository: TasksRepository) : ViewModel(),
    ErrorHolderDelegate by errorHolderDelegate {
    val pendingTaskList: LiveData<List<Task>>
        get() = tasksRepository.pendingTasks
            .asLiveData()

    fun selectPendingTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            handleRepositoryResult(tasksRepository.updateTask(task.copy(), true))
        }
    }
}