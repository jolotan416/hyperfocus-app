package com.spotlight.spotlightapp.utilities

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.spotlight.spotlightapp.MainActivity
import com.spotlight.spotlightapp.R
import com.spotlight.spotlightapp.utilities.notification.NotificationCountDownTimer
import com.spotlight.spotlightapp.utilities.notification.NotificationType
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationModule @Inject constructor() {
    companion object {
        private const val TASK_NOTIFICATION_CHANNEL_ID = "task_notification_channel"
    }

    private var currentCountDownTimer: NotificationCountDownTimer? = null

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
        notificationBuilder.setSmallIcon(R.drawable.ic_check_circle)

        val notification = when (notificationType) {
            is NotificationType.TimerNotification -> {
                createTimerNotification(context, notificationBuilder, notificationType)
            }
        }

        return notification
    }

    private fun createTimerNotification(
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

        currentCountDownTimer = NotificationCountDownTimer(
            context, notificationBuilder, timerNotification, timerEndTimeDifference)
        currentCountDownTimer!!.start()

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(
                context, NotificationType.getIdFromNotificationType(timerNotification.javaClass),
                intent, PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getActivity(
                context, NotificationType.getIdFromNotificationType(timerNotification.javaClass),
                intent, 0)
        }
        notificationBuilder.setContentIntent(pendingIntent)

        return notificationBuilder.build()
    }

    fun stopNotificationTimer() {
        currentCountDownTimer?.cancelNotificationTimer()
    }
}