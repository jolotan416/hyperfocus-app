package com.spotlight.spotlightapp.task.services

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import com.spotlight.spotlightapp.data.task.Task
import com.spotlight.spotlightapp.task.repo.TasksRepository
import com.spotlight.spotlightapp.utilities.NotificationModule
import com.spotlight.spotlightapp.utilities.notification.NotificationType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TaskTimerService : Service() {
    companion object {
        const val RUN_TASK = "run_task"
        const val RUNNING_TASK = "running_task"
        const val STOP_TASK = "stop_task"
    }

    @Inject
    lateinit var notificationModule: NotificationModule

    @Inject
    lateinit var repository: TasksRepository

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            RUN_TASK -> {
                startForeground(intent.getParcelableExtra(RUNNING_TASK)!!)
            }
            STOP_TASK -> {
                notificationModule.stopNotificationTimer()
                stopSelf()
            }
        }

        return START_REDELIVER_INTENT
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun startForeground(task: Task) {
        val timerNotificationType = NotificationType.TimerNotification(
            task.title, task.currentTimerEndDate!!.time) {
            CoroutineScope(Dispatchers.IO).launch {
                repository.toggleTaskTimer(task, false)
            }
            stopForeground()
        }
        val notification = notificationModule.createNotification(
            applicationContext, timerNotificationType)

        startForeground(
            NotificationType.getIdFromNotificationType(
                timerNotificationType::class.java), notification)
    }

    private fun stopForeground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(STOP_FOREGROUND_DETACH)
        } else {
            stopForeground(false)
        }
    }
}