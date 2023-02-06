package com.spotlight.spotlightapp.utilities.viewmodelutils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.spotlight.spotlightapp.data.ErrorEntity
import com.spotlight.spotlightapp.data.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface ErrorHolder {
    val snackbarErrorMessageRes: LiveData<ErrorEntity?>
    fun notifySnackbarMessageShown()
    suspend fun <R> handleRepositoryResult(
        result: Result<R>, successHandling: (Result.Success<R>) -> Unit = {})
}

class ErrorHolderImpl : ErrorHolder {
    private val mSnackbarErrorMessageRes: MutableLiveData<ErrorEntity?> by lazy {
        MutableLiveData<ErrorEntity?>(null)
    }

    override val snackbarErrorMessageRes: LiveData<ErrorEntity?> = mSnackbarErrorMessageRes

    override fun notifySnackbarMessageShown() {
        mSnackbarErrorMessageRes.value = null
    }

    override suspend fun <R> handleRepositoryResult(
        result: Result<R>, successHandling: (Result.Success<R>) -> Unit) {
        withContext(Dispatchers.Main) {
            when (result) {
                is Result.Error -> mSnackbarErrorMessageRes.value = result.errorEntity
                is Result.Success -> successHandling(result)
            }
        }
    }
}