package com.spotlight.spotlightapp.data.task

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Entity
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "priority", defaultValue = "0")
    val priority: Int = 0,

    @ColumnInfo(name = "is_finished", defaultValue = "false")
    val isFinished: Boolean = false,

    @ColumnInfo(name = "current_timer_end_date", defaultValue = "0")
    val currentTimerEndDate: Date? = null,

    @Embedded(prefix = "alert_interval_")
    val alertInterval: TaskAlertInterval = TaskAlertInterval()
) : Parcelable {
    val isInDailyIntentList: Boolean
        get() = priority != 0
}