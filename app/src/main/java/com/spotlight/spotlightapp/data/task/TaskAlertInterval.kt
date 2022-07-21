package com.spotlight.spotlightapp.data.task

import android.content.res.Resources
import android.os.Parcelable
import androidx.annotation.PluralsRes
import androidx.room.ColumnInfo
import com.spotlight.spotlightapp.R
import kotlinx.parcelize.Parcelize
import java.util.*
import kotlin.math.max

@Parcelize
data class TaskAlertInterval(
    @ColumnInfo(name = "amount", defaultValue = "1")
    val amount: Int = 1,

    @ColumnInfo("unit", defaultValue = "0")
    val unit: Unit = Unit.HOUR
) : Parcelable {
    enum class Unit(
        val calendarUnit: Int, val maxAmount: Int, @PluralsRes val labelPluralRes: Int) {
        HOUR(Calendar.HOUR, 4, R.plurals.hours),
        MINUTE(Calendar.MINUTE, 240, R.plurals.minutes);

        companion object {
            fun getPosition(unit: Unit) = max(values().indexOf(unit), 0)
            fun getLabelList(resources: Resources, amount: Int): List<String> =
                values().map { resources.getQuantityString(it.labelPluralRes, amount) }
        }
    }
}