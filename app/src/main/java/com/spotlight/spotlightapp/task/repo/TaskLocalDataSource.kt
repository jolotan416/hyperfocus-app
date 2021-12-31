package com.spotlight.spotlightapp.task.repo

import com.spotlight.spotlightapp.data.dao.TaskDao
import com.spotlight.spotlightapp.data.task.Task
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskLocalDataSource @Inject constructor(private val taskDao: TaskDao) {
    val tasks: Flow<List<Task>>
        get() = taskDao.getTasks()

    fun insertTask(task: Task) {
        taskDao.insertTask(task)
    }

    fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }
}