package com.spotlight.spotlightapp.data.converter

import androidx.room.TypeConverter
import java.util.*

class DateConverter {
    @TypeConverter
    fun getDateFromLong(dateLongValue: Long?): Date? = dateLongValue?.let { Date(it) }

    @TypeConverter
    fun getLongFromDate(date: Date?): Long? = date?.time
}