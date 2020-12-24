package com.spotlight.spotlightapp.data.dao

import androidx.room.*
import com.spotlight.spotlightapp.data.task.Task

@Dao
interface TaskDao {
    @Query("SELECT * FROM task")
    fun getTasks(): List<Task>

    @Query("SELECT * FROM task WHERE is_finished = :isFinished")
    fun getTasks(isFinished: Boolean): List<Task>

    @Query("SELECT * FROM task WHERE priority != 0")
    fun getDailyIntentList(): List<Task>

    @Insert
    fun insertTask(task: Task)

    @Update
    fun updateTask(task: Task)

    @Delete
    fun deleteTask(task: Task)
}