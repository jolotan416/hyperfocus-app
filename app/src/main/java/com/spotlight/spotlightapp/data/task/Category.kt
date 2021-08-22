package com.spotlight.spotlightapp.data.task

import android.graphics.Color
import android.os.Parcelable
import androidx.annotation.ColorInt
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Category(
    @PrimaryKey(autoGenerate = true)
    val categoryId: Int = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "color")
    val color: String
) : Parcelable {
    @ColorInt
    fun getColorInt(): Int = Color.parseColor(color)
}