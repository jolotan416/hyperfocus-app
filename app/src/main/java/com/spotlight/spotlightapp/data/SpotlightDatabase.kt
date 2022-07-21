package com.spotlight.spotlightapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.spotlight.spotlightapp.data.converter.DateConverter
import com.spotlight.spotlightapp.data.converter.TaskAlertIntervalUnitConverter
import com.spotlight.spotlightapp.data.dao.TaskDao
import com.spotlight.spotlightapp.data.task.Task

@Database(entities = [Task::class], version = 1)
@TypeConverters(TaskAlertIntervalUnitConverter::class, DateConverter::class)
abstract class SpotlightDatabase : RoomDatabase() {
    abstract fun getTaskDao(): TaskDao
}