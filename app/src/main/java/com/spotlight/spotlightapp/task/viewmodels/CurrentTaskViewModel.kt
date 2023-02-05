package com.spotlight.spotlightapp.task.viewmodels

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spotlight.spotlightapp.data.task.Task
import com.spotlight.spotlightapp.data.task.TaskAlertInterval
import com.spotlight.spotlightapp.task.repo.TasksRepository
import com.spotlight.spotlightapp.task.viewdata.CurrentTaskUIState
import com.spotlight.spotlightapp.task.viewdata.TaskCountDownData
import com.spotlight.spotlightapp.utilities.viewmodelutils.ErrorHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class CurrentTaskViewModel @Inject constructor(
    private val errorHolder: ErrorHolder, private val tasksRepository: TasksRepository) :
    ViewModel() {
    companion object {
        private const val TIMER_INTERVAL_IN_MILLIS = 1000L
        private const val TIME_CONVERSION_VALUE = 60
    }

    private var taskCountDownTimer: CountDownTimer? = null
    private var taskFlowObserverJob: Job? = null

    private val mCurrentTaskUIState: MutableLiveData<CurrentTaskUIState> by lazy {
        MutableLiveData<CurrentTaskUIState>()
    }

    val currentTaskUIState: LiveData<CurrentTaskUIState> = mCurrentTaskUIState

    fun setCurrentTask(currentTask: Task, willAllowEdit: Boolean) {
        mCurrentTaskUIState.value = CurrentTaskUIState(currentTask, willAllowEdit)
        observeTask(currentTask.id)
    }

    fun setCurrentTaskAlertInterval(taskAlertInterval: TaskAlertInterval) {
        val task = currentTaskUIState.value!!.task.copy(alertInterval = taskAlertInterval)
        viewModelScope.launch(Dispatchers.IO) {
            errorHolder.handleRepositoryResult(tasksRepository.updateTask(task))
        }
    }

    fun toggleTaskAlertTimer(isTimerRunning: Boolean) {
        val task = currentTaskUIState.value!!.task
        viewModelScope.launch(Dispatchers.IO) {
            errorHolder.handleRepositoryResult(
                tasksRepository.toggleTaskTimer(task, isTimerRunning))
        }
    }

    fun deleteTask() {
        val task = currentTaskUIState.value!!.task
        viewModelScope.launch(Dispatchers.IO) {
            errorHolder.handleRepositoryResult(tasksRepository.deleteTask(task.copy())) { result ->
                mCurrentTaskUIState.value = currentTaskUIState.value!!.copy(
                    deleteTaskResult = result)
            }
        }
    }

    fun toggleTaskFinished() {
        viewModelScope.launch(Dispatchers.IO) {
            errorHolder.handleRepositoryResult(
                tasksRepository.toggleTaskFinished(
                    currentTaskUIState.value!!.task.copy())) { result ->
                mCurrentTaskUIState.value = currentTaskUIState.value!!.copy(
                    task = result.content, completeTaskResult = result)
            }
        }
    }

    private fun observeTask(taskId: Int) {
        taskFlowObserverJob?.cancel()
        taskFlowObserverJob = viewModelScope.launch(Dispatchers.IO) {
            tasksRepository.observeTask(taskId).collect { task ->
                val currentTimerEndDate = task?.currentTimerEndDate
                if (currentTimerEndDate == null) {
                    taskCountDownTimer?.cancel()
                }

                withContext(Dispatchers.Main) {
                    mCurrentTaskUIState.value = currentTaskUIState.value!!.copy(
                        task = task ?: return@withContext,
                        taskCountDownData = TaskCountDownData("", 0f, true))
                }
                currentTimerEndDate?.time?.let {
                    startCountDownTimer(it - Calendar.getInstance().timeInMillis)
                }
            }
        }
    }

    private fun startCountDownTimer(timerEndTimeDifference: Long) {
        viewModelScope.launch(Dispatchers.Main) {
            taskCountDownTimer =
                object : CountDownTimer(timerEndTimeDifference, TIMER_INTERVAL_IN_MILLIS) {
                    override fun onTick(millisUntilFinished: Long) {
                        val countDownTimerString = String.format(
                            "%02d:%02d:%02d",
                            TimeUnit.MILLISECONDS.toHours(
                                millisUntilFinished) % TIME_CONVERSION_VALUE,
                            TimeUnit.MILLISECONDS.toMinutes(
                                millisUntilFinished) % TIME_CONVERSION_VALUE,
                            TimeUnit.MILLISECONDS.toSeconds(
                                millisUntilFinished) % TIME_CONVERSION_VALUE)
                        mCurrentTaskUIState.value = currentTaskUIState.value!!.copy(
                            taskCountDownData = TaskCountDownData(
                                countDownTimerString,
                                (timerEndTimeDifference - millisUntilFinished) / timerEndTimeDifference.toFloat(),
                                false))
                    }

                    override fun onFinish() {
                        mCurrentTaskUIState.value = currentTaskUIState.value!!.copy(
                            taskCountDownData = null)
                    }
                }
            taskCountDownTimer!!.start()
        }
    }
}