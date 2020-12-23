package com.spotlight.spotlightapp.data.dao

import androidx.room.*
import com.spotlight.spotlightapp.data.task.Task

@Dao
interface TaskDao {
    @Query("SELECT * FROM task")
    fun getTaskList(): List<Task>

    @Insert
    fun insertTask(task: Task)

    @Update
    fun updateTask(task: Task)

    @Delete
    fun deleteTask(task: Task)
}