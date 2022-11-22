package com.spotlight.spotlightapp.utilities

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.CountDownTimer
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.spotlight.spotlightapp.R
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationModule @Inject constructor() {
    companion object {
        private const val TASK_NOTIFICATION_CHANNEL_ID = "task_notification_channel"
        private const val TIMER_INTERVAL_IN_MILLISECONDS = 1000L
    }

    sealed class NotificationType(val id: Int) {
        data class TimerNotification(
            val title: String, val timerEndTime: Long, val onFinish: () -> Unit) :
            NotificationType(1)
    }

    fun createNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val taskNotificationChannelName = context.getString(
                R.string.task_notification_channel_name)
            val taskNotificationChannel = NotificationChannel(
                TASK_NOTIFICATION_CHANNEL_ID, taskNotificationChannelName,
                NotificationManager.IMPORTANCE_DEFAULT)
            NotificationManagerCompat.from(context)
                .createNotificationChannel(taskNotificationChannel)
        }
    }

    fun createNotification(
        context: Context, notificationType: NotificationType): Notification {
        val notificationBuilder = NotificationCompat.Builder(context, TASK_NOTIFICATION_CHANNEL_ID)
        notificationBuilder.setSmallIcon(R.drawable.ic_launcher_foreground)

        val notification = when (notificationType) {
            is NotificationType.TimerNotification -> {
                createNotification(context, notificationBuilder, notificationType)
            }
        }

        return notification
    }

    private fun createNotification(
        context: Context,
        notificationBuilder: NotificationCompat.Builder,
        timerNotification: NotificationType.TimerNotification): Notification {
        val timerStartTime = Calendar.getInstance().timeInMillis
        val timerEndTimeDifference = timerNotification.timerEndTime - timerStartTime

        notificationBuilder.setContentTitle(timerNotification.title)
            .setShowWhen(true)
            .setUsesChronometer(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            notificationBuilder.foregroundServiceBehavior = Notification.FOREGROUND_SERVICE_IMMEDIATE
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            notificationBuilder.setWhen(timerNotification.timerEndTime)
                .setChronometerCountDown(true)
        } else {
            notificationBuilder.setWhen(timerStartTime - timerEndTimeDifference)
        }

        val countDownTimer = object : CountDownTimer(
            timerEndTimeDifference,
            TIMER_INTERVAL_IN_MILLISECONDS) {
            override fun onTick(millisUntilFinished: Long) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) return

                notificationBuilder.setWhen(
                    Calendar.getInstance().timeInMillis - millisUntilFinished)
                NotificationManagerCompat.from(context)
                    .notify(timerNotification.id, notificationBuilder.build())
            }

            override fun onFinish() {
                notificationBuilder.setContentText(
                    context.getString(R.string.finished_task_notification_text))
                    .setShowWhen(false)
                    .setUsesChronometer(false)
                NotificationManagerCompat.from(context)
                    .notify(timerNotification.id, notificationBuilder.build())
                timerNotification.onFinish()
            }
        }
        countDownTimer.start()

        return notificationBuilder.build()
    }
}