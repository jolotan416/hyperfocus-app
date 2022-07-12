package com.spotlight.spotlightapp.task.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.spotlight.spotlightapp.task.viewdata.CurrentTaskAlertInterval
import kotlin.math.max
import kotlin.math.min

class CurrentTaskAlertIntervalViewModel : ViewModel() {
    companion object {
        private const val MINIMUM_AMOUNT = 0
    }

    private val mCurrentTaskAlertInterval: MutableLiveData<CurrentTaskAlertInterval> by lazy {
        MutableLiveData<CurrentTaskAlertInterval>()
    }

    val currentTaskAlertInterval: LiveData<CurrentTaskAlertInterval> = mCurrentTaskAlertInterval

    fun setCurrentTaskAlertInterval(currentTaskAlertInterval: CurrentTaskAlertInterval) {
        mCurrentTaskAlertInterval.value = currentTaskAlertInterval
    }

    fun setAlertIntervalUnit(unit: CurrentTaskAlertInterval.Unit) {
        validateMaxAlertInterval(currentTaskAlertInterval.value!!.amount, unit)
    }

    fun setAlertAmount(amount: String) {
        validateMaxAlertInterval(
            amount.toIntOrNull() ?: MINIMUM_AMOUNT, currentTaskAlertInterval.value!!.unit)
    }

    private fun validateMaxAlertInterval(amount: Int, unit: CurrentTaskAlertInterval.Unit) {
        mCurrentTaskAlertInterval.value = CurrentTaskAlertInterval(
            min(max(amount, MINIMUM_AMOUNT), unit.maxAmount), unit)
    }
}