package com.spotlight.spotlightapp.worker

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.spotlight.spotlightapp.task.repo.TasksRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltWorker
class TaskCleanupWorker @AssistedInject constructor(
    @Assisted appContext: Context, @Assisted workerParameters: WorkerParameters,
    private val tasksRepository: TasksRepository) :
    Worker(appContext, workerParameters) {
    companion object {
        const val TAG = "TaskCleanupWorker"
    }

    @SuppressLint("RestrictedApi")
    override fun doWork(): Result {
        Log.d(TAG, "Work started.")
        return try {
            CoroutineScope(Dispatchers.IO).launch {
                tasksRepository.resetDailyIntentListPriority()
            }

            Result.Success()
        } catch (exception: Exception) {
            Log.e(TAG, "Encountered exception when cleaning up tasks: $exception")

            Result.Retry()
        }
    }
}