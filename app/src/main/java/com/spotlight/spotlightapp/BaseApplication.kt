package com.spotlight.spotlightapp

import android.app.Application
import com.spotlight.spotlightapp.data.SpotlightDatabaseHolder
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        SpotlightDatabaseHolder.createInstance(applicationContext)
    }
}