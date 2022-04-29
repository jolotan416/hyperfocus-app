package com.spotlight.spotlightapp.task

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.spotlight.spotlightapp.R
import kotlin.reflect.KClass

enum class TaskListType(
    @StringRes val titleRes: Int) {
    PENDING(R.string.pending),
    COMPLETED(R.string.completed)
}