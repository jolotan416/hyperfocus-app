package com.spotlight.spotlightapp.data.task

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "description")
    var description: String,

    @ColumnInfo(name = "priority")
    var priority: Int,

    @ColumnInfo(name = "category_id")
    var categoryId: Int,

    @ColumnInfo(name = "is_finished", defaultValue = "false")
    var isFinished: Boolean = false
)