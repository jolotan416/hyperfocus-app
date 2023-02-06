package com.spotlight.spotlightapp.task.viewmodels

import com.spotlight.spotlightapp.utilities.viewmodelutils.ErrorHolder
import com.spotlight.spotlightapp.utilities.viewmodelutils.ErrorHolderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class ViewModelModule {
    @Provides
    fun provideErrorHolder(): ErrorHolder = ErrorHolderImpl()
}