package com.spotlight.spotlightapp.task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spotlight.spotlightapp.data.task.Task
import com.spotlight.spotlightapp.task.repo.TasksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

// TODO: Add save state to ViewModel
@HiltViewModel
class PendingTaskListViewModel @Inject constructor(private val tasksRepository: TasksRepository)
    : ViewModel() {
    companion object {
        private const val MAX_PRIORITY = 5
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            tasksRepository.pendingTasks.collect { tasks ->
                maxPendingTaskListPriority = tasks.maxOfOrNull { it.priority } ?: 0

                withContext(Dispatchers.Main) {
                    mPendingTaskList.value = tasks
                }
            }
        }
    }

    private var maxPendingTaskListPriority: Int = 0

    private val mPendingTaskList: MutableLiveData<List<Task>> by lazy {
        MutableLiveData<List<Task>>()
    }

    val pendingTaskList: LiveData<List<Task>>
        get() = mPendingTaskList

    fun selectPendingTask(task: Task) {
        when {
            task.priority != 0 -> {
                pendingTaskList.value
                    ?.filter { it.priority > task.priority }
                    ?.forEach { succeedingTask ->
                        updateTaskPriority(succeedingTask, succeedingTask.priority - 1)
                    }

                updateTaskPriority(task, 0)
            }
            maxPendingTaskListPriority >= MAX_PRIORITY -> {
                // TODO: Show error message when user selects more than 5 tasks
            }
            else -> {
                updateTaskPriority(task, ++maxPendingTaskListPriority)
            }
        }
    }

    private fun updateTaskPriority(task: Task, priority: Int) {
        val taskCopy = task.copy()
        taskCopy.priority = priority
        viewModelScope.launch(Dispatchers.IO) {
            tasksRepository.updateTask(taskCopy)
        }
    }
}