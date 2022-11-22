package com.spotlight.spotlightapp

import android.app.Application
import com.spotlight.spotlightapp.utilities.NotificationModule
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class BaseApplication : Application() {
    @Inject
    lateinit var notificationModule: NotificationModule

    override fun onCreate() {
        super.onCreate()

        notificationModule.createNotificationChannels(applicationContext)
    }
}