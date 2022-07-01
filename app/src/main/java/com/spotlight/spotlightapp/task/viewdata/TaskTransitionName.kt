package com.spotlight.spotlightapp.task.viewdata

enum class TaskTransitionName(private val transitionName: String) {
    TASK_LIST("TaskList"),
    CURRENT_TASK("CurrentTask"),
    TASK_FORM("TaskForm");

    fun getTransitionName(taskId: String = "") = transitionName + taskId
}