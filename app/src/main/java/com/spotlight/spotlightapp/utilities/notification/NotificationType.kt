package com.spotlight.spotlightapp.utilities.notification

sealed interface NotificationType {
    data class TimerNotification(
        val title: String, val timerEndTime: Long,
        val onFinish: () -> Unit) : NotificationType

    companion object {
        fun getIdFromNotificationType(notificationType: Class<out NotificationType>): Int =
            when (notificationType) {
                TimerNotification::class.java -> 1
                else -> 0
            }
    }
}