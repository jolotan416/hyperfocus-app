package com.spotlight.spotlightapp.data.task

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "description")
    var description: String,

    @ColumnInfo(name = "priority", defaultValue = "0")
    var priority: Int = 0,

    @Embedded
    var category: Category,

    @ColumnInfo(name = "is_finished", defaultValue = "false")
    var isFinished: Boolean = false
) : Parcelable