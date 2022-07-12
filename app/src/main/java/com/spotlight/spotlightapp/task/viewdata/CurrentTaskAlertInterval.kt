package com.spotlight.spotlightapp.task.viewdata

import android.content.res.Resources
import android.os.Parcelable
import androidx.annotation.PluralsRes
import com.spotlight.spotlightapp.R
import kotlinx.parcelize.Parcelize
import java.util.*
import kotlin.math.max

@Parcelize
data class CurrentTaskAlertInterval(
    val amount: Int = 1,
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