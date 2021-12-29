package com.spotlight.spotlightapp.task

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.spotlight.spotlightapp.R
import kotlin.reflect.KClass

enum class TaskListType(
    @StringRes val titleRes: Int,
    val fragmentClass: KClass<out Fragment>) {
    PENDING(R.string.pending, PendingTaskListFragment::class),
    // TODO: Create CompletedTaskListFragment
    COMPLETED(R.string.completed, PendingTaskListFragment::class)
}