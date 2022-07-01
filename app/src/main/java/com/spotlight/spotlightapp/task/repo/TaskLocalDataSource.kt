package com.spotlight.spotlightapp.task.repo

import com.spotlight.spotlightapp.data.dao.TaskDao
import com.spotlight.spotlightapp.data.task.Task
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskLocalDataSource @Inject constructor(private val taskDao: TaskDao) {
    fun observeTasks(): Flow<List<Task>> = taskDao.observeTasks()
    fun observeTask(taskId: Int): Flow<Task> = taskDao.observeTask(taskId)

    suspend fun getTasks(): List<Task> = taskDao.getTasks()

    suspend fun insertTask(task: Task) {
        taskDao.insertTask(task)
    }

    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }
}