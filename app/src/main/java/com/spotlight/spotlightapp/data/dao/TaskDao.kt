package com.spotlight.spotlightapp.data.dao

import androidx.room.*
import com.spotlight.spotlightapp.data.task.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM task")
    fun observeTasks(): Flow<List<Task>>

    @Query("SELECT * FROM task")
    suspend fun getTasks(): List<Task>

    @Query("SELECT * FROM task WHERE id == :taskId")
    fun observeTask(taskId: Int): Flow<Task?>

    @Insert
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)
}