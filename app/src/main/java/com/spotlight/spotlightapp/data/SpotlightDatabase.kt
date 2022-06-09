package com.spotlight.spotlightapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.spotlight.spotlightapp.data.dao.TaskDao
import com.spotlight.spotlightapp.data.task.Task

@Database(entities = [Task::class], version = 1)
abstract class SpotlightDatabase : RoomDatabase() {
    abstract fun getTaskDao(): TaskDao
}