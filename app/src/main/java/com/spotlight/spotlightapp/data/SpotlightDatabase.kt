package com.spotlight.spotlightapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.spotlight.spotlightapp.data.dao.TaskDao
import com.spotlight.spotlightapp.data.task.Task
import com.spotlight.spotlightapp.data.task.TaskAlertIntervalUnitConverter

@Database(entities = [Task::class], version = 1)
@TypeConverters(TaskAlertIntervalUnitConverter::class)
abstract class SpotlightDatabase : RoomDatabase() {
    abstract fun getTaskDao(): TaskDao
}