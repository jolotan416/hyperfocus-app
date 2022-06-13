package com.spotlight.spotlightapp.data

import android.content.Context
import androidx.room.Room
import com.spotlight.spotlightapp.data.dao.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    private const val DATABASE_NAME = "spotlight_database"

    @Singleton
    @Provides
    fun provideSpotlightDatabase(@ApplicationContext context: Context): SpotlightDatabase =
        Room.databaseBuilder(context, SpotlightDatabase::class.java, DATABASE_NAME)
            .build()

    @Singleton
    @Provides
    fun provideTaskDao(database: SpotlightDatabase): TaskDao = database.getTaskDao()
}