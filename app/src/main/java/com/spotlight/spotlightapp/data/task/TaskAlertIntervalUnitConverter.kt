package com.spotlight.spotlightapp.data.task

import androidx.room.TypeConverter

class TaskAlertIntervalUnitConverter {
    @TypeConverter
    fun getTaskAlertIntervalUnitFromInt(taskAlertIntervalUnitInt: Int): TaskAlertInterval.Unit? =
        TaskAlertInterval.Unit.values().getOrNull(taskAlertIntervalUnitInt)

    @TypeConverter
    fun getIntFromTaskAlertIntervalUnit(taskAlertIntervalUnit: TaskAlertInterval.Unit): Int =
        TaskAlertInterval.Unit.values().indexOf(taskAlertIntervalUnit)
}