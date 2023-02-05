package com.spotlight.spotlightapp.utilities.viewmodelutils

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class ViewModelModule {
    @Provides
    fun providesErrorHolderDelegate(): ErrorHolderDelegate = ErrorHolderDelegateImpl()
}