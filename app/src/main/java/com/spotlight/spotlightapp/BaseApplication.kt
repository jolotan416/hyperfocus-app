package com.spotlight.spotlightapp

import android.app.Application
import com.spotlight.spotlightapp.data.SpotlightDatabaseHolder

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        SpotlightDatabaseHolder.createInstance(applicationContext)
    }
}