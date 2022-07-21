package com.spotlight.spotlightapp.task.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.spotlight.spotlightapp.data.task.TaskAlertInterval
import kotlin.math.max
import kotlin.math.min

class CurrentTaskAlertIntervalViewModel : ViewModel() {
    companion object {
        private const val MINIMUM_AMOUNT = 0
    }

    private val mCurrentTaskAlertInterval: MutableLiveData<TaskAlertInterval> by lazy {
        MutableLiveData<TaskAlertInterval>()
    }

    val currentTaskAlertInterval: LiveData<TaskAlertInterval> = mCurrentTaskAlertInterval

    fun setCurrentTaskAlertInterval(taskAlertInterval: TaskAlertInterval) {
        mCurrentTaskAlertInterval.value = taskAlertInterval
    }

    fun setAlertIntervalUnit(unit: TaskAlertInterval.Unit) {
        validateMaxAlertInterval(currentTaskAlertInterval.value!!.amount, unit)
    }

    fun setAlertAmount(amount: String) {
        validateMaxAlertInterval(
            amount.toIntOrNull() ?: MINIMUM_AMOUNT, currentTaskAlertInterval.value!!.unit)
    }

    private fun validateMaxAlertInterval(amount: Int, unit: TaskAlertInterval.Unit) {
        mCurrentTaskAlertInterval.value = TaskAlertInterval(
            min(max(amount, MINIMUM_AMOUNT), unit.maxAmount), unit)
    }
}