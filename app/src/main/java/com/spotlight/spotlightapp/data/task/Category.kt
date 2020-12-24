package com.spotlight.spotlightapp.data.task

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Category(
    @PrimaryKey(autoGenerate = true)
    val categoryId: Int = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "color")
    val color: String
) {
    @ColorInt
    fun getColorInt(): Int = Color.parseColor(color)
}