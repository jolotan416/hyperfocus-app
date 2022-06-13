package com.spotlight.spotlightapp.utilities

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.spotlight.spotlightapp.data.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

open class BaseViewModel : ViewModel() {
    private val mSnackbarErrorMessageRes: MutableLiveData<Int?> by lazy {
        MutableLiveData<Int?>(null)
    }

    val snackbarErrorMessageRes: LiveData<Int?> = mSnackbarErrorMessageRes

    fun notifySnackbarMessageShown() {
        mSnackbarErrorMessageRes.value = null
    }

    protected suspend fun <R> handleRepositoryResult(
        result: Result<R>, successHandling: () -> Unit = {}) {
        withContext(Dispatchers.Main) {
            if (result is Result.Error) {
                mSnackbarErrorMessageRes.value = result.errorEntity.errorMessageRes
            } else {
                successHandling()
            }
        }
    }
}