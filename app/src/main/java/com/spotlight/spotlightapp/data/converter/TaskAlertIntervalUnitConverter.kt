package com.spotlight.spotlightapp.data.converter

import androidx.room.TypeConverter
import com.spotlight.spotlightapp.data.task.TaskAlertInterval

class TaskAlertIntervalUnitConverter {
    @TypeConverter
    fun getTaskAlertIntervalUnitFromInt(taskAlertIntervalUnitInt: Int): TaskAlertInterval.Unit? =
        TaskAlertInterval.Unit.values().getOrNull(taskAlertIntervalUnitInt)

    @TypeConverter
    fun getIntFromTaskAlertIntervalUnit(taskAlertIntervalUnit: TaskAlertInterval.Unit): Int =
        TaskAlertInterval.Unit.values().indexOf(taskAlertIntervalUnit)
}