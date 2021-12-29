package com.spotlight.spotlightapp.data

import com.spotlight.spotlightapp.data.dao.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object TaskModule {
    @Provides
    fun provideSpotlightDatabase(): TaskDao = SpotlightDatabaseHolder.getInstance()
        .getTaskDao()
}