package com.spotlight.spotlightapp.data.task

import android.os.Parcelable
import androidx.room.ColumnInfo
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class TaskTimerData(
    @ColumnInfo(name = "current_timer_start_date")
    val currentTimerStartDate: Date,

    @ColumnInfo(name = "current_timer_end_date")
    val currentTimerEndDate: Date
) : Parcelable