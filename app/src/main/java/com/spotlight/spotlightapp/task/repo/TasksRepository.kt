package com.spotlight.spotlightapp.task.repo

import com.spotlight.spotlightapp.data.task.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TasksRepository @Inject constructor(private val localDataSource: TaskLocalDataSource) {
    val pendingTasks: Flow<List<Task>>
        get() = localDataSource.tasks
            .map { tasks -> sortTasks(tasks.filter { !it.isFinished }) }

    val completedTasks: Flow<List<Task>>
        get() = localDataSource.tasks
            .map { tasks -> sortTasks(tasks.filter { it.isFinished }) }

    val dailyIntentList: Flow<List<Task>>
        get() = localDataSource.tasks
            .map { tasks ->
                tasks.filter { it.priority != 0 }
                    .sortedBy { it.priority }
            }

    fun insertTask(task: Task) {
        localDataSource.insertTask(task)
    }

    private fun sortTasks(tasks: List<Task>) =
        tasks.filter { it.priority != 0 }.sortedBy { it.priority } +
                tasks.filter { it.priority == 0 }
}