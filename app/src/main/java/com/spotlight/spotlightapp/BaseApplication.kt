package com.spotlight.spotlightapp

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import com.spotlight.spotlightapp.utilities.NotificationModule
import com.spotlight.spotlightapp.worker.TaskCleanupWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class BaseApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var notificationModule: NotificationModule

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()

        notificationModule.createNotificationChannels(applicationContext)
        startTaskCleanupWorker()
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }

    private fun startTaskCleanupWorker() {
        val calendar = Calendar.getInstance()
        val currentTimeInMillis = calendar.timeInMillis

        calendar.apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val startOfDayInMillis = calendar.timeInMillis - currentTimeInMillis
        val initialDelayInMillis = if (startOfDayInMillis >= 0) startOfDayInMillis else calendar.run {
            add(Calendar.DATE, 1)
            timeInMillis - currentTimeInMillis
        }

        val taskCleanupWorkRequest = PeriodicWorkRequestBuilder<TaskCleanupWorker>(
            1, TimeUnit.DAYS)
            .setConstraints(Constraints(requiresDeviceIdle = true))
            .setInitialDelay(initialDelayInMillis, TimeUnit.MILLISECONDS)
            .addTag(TaskCleanupWorker.TAG)
            .build()

        WorkManager.getInstance(applicationContext)
            .enqueueUniquePeriodicWork(
                TaskCleanupWorker.TAG, ExistingPeriodicWorkPolicy.KEEP, taskCleanupWorkRequest)
    }
}