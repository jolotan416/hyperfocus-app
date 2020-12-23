package com.spotlight.spotlightapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.spotlight.spotlightapp.data.dao.CategoryDao
import com.spotlight.spotlightapp.data.dao.TaskDao
import com.spotlight.spotlightapp.data.task.Category
import com.spotlight.spotlightapp.data.task.Task

object SpotlightDatabaseFactory {
    fun createInstance(context: Context) {
        instance = Room.databaseBuilder(
            context,
            SpotlightDatabase::class.java, DATABASE_NAME)
            .build()
    }

    fun getInstance() = instance

    private const val DATABASE_NAME = "spotlight_database"
    private lateinit var instance: SpotlightDatabase
}

@Database(entities = [Category::class, Task::class], version = 1)
abstract class SpotlightDatabase : RoomDatabase() {
    abstract fun getCategoryDao(): CategoryDao
    abstract fun getTaskDao(): TaskDao
}