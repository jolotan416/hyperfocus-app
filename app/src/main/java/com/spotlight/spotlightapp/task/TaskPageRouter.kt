package com.spotlight.spotlightapp.task

import android.view.View
import com.spotlight.spotlightapp.data.task.Task

interface TaskPageRouter {
    fun openTaskList(view: View)
    fun openTaskPage(view: View, task: Task, willAllowEdit: Boolean)
    fun openTaskForm(view: View, task: Task? = null)
}