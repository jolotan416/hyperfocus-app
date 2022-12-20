package com.spotlight.spotlightapp.utilities.notification

import android.content.Context
import android.os.Build
import android.os.CountDownTimer
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.spotlight.spotlightapp.R
import java.util.*

class NotificationCountDownTimer(
    private val context: Context, private val notificationBuilder: NotificationCompat.Builder,
    private val timerNotification: NotificationType.TimerNotification,
    timerEndTimeDifference: Long) :
    CountDownTimer(timerEndTimeDifference, TIMER_INTERVAL_IN_MILLISECONDS) {
    companion object {
        private const val TIMER_INTERVAL_IN_MILLISECONDS = 1000L
    }

    override fun onTick(millisUntilFinished: Long) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) return

        notificationBuilder.setWhen(
            Calendar.getInstance().timeInMillis - millisUntilFinished)
        NotificationManagerCompat.from(context)
            .notify(
                NotificationType.getIdFromNotificationType(
                    timerNotification.javaClass),
                notificationBuilder.build())
    }

    override fun onFinish() {
        notificationBuilder.setContentText(
            context.getString(R.string.finished_task_timer_notification_text))
            .setShowWhen(false)
            .setUsesChronometer(false)
        NotificationManagerCompat.from(context)
            .notify(
                NotificationType.getIdFromNotificationType(
                    timerNotification.javaClass),
                notificationBuilder.build())
        timerNotification.onFinish()
    }

    fun cancelNotificationTimer() {
        cancel()
        timerNotification.onFinish()
        NotificationManagerCompat.from(context)
            .cancel(NotificationType.getIdFromNotificationType(timerNotification.javaClass))
    }
}