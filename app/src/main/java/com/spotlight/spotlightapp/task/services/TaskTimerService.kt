package com.spotlight.spotlightapp.task.services

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import com.spotlight.spotlightapp.data.task.Task
import com.spotlight.spotlightapp.task.repo.TasksRepository
import com.spotlight.spotlightapp.utilities.NotificationModule
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TaskTimerService : Service() {
    companion object {
        const val RUNNING_TASK = "running_task"
    }

    @Inject
    lateinit var notificationModule: NotificationModule

    @Inject
    lateinit var repository: TasksRepository

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(
            intent?.getParcelableExtra(RUNNING_TASK)
                ?: return START_STICKY_COMPATIBILITY)

        return START_REDELIVER_INTENT
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun startForeground(task: Task) {
        val timerNotificationType = NotificationModule.NotificationType.TimerNotification(
            task.title, task.currentTimerEndDate!!.time) {
            CoroutineScope(Dispatchers.IO).launch {
                repository.toggleTaskTimer(task, false)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                stopForeground(STOP_FOREGROUND_DETACH)
            } else {
                stopForeground(false)
            }
        }
        val notification = notificationModule.createNotification(
            applicationContext, timerNotificationType)

        startForeground(timerNotificationType.id, notification)
    }
}