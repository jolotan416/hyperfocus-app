package com.spotlight.spotlightapp.task.viewdata

import com.spotlight.spotlightapp.data.Result
import com.spotlight.spotlightapp.data.task.Task

data class CurrentTaskUIState(
    val task: Task,
    val willShowEditButtons: Boolean,
    val completeTaskResult: Result<Task>? = null
)