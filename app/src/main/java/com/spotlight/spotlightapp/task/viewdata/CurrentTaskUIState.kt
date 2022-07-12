package com.spotlight.spotlightapp.task.viewdata

import com.spotlight.spotlightapp.data.Result
import com.spotlight.spotlightapp.data.task.Task

data class CurrentTaskUIState(
    var task: Task,
    var willShowEditButtons: Boolean,
    var currentTaskAlertInterval: CurrentTaskAlertInterval = CurrentTaskAlertInterval(),
    var completeTaskResult: Result<Task>? = null,
    var deleteTaskResult: Result<Any?>? = null
)