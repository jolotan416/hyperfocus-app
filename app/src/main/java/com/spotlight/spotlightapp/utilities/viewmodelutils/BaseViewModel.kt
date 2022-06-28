package com.spotlight.spotlightapp.utilities.viewmodelutils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.spotlight.spotlightapp.data.ErrorEntity
import com.spotlight.spotlightapp.data.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

open class BaseViewModel : ViewModel() {
    private val mSnackbarErrorMessageRes: MutableLiveData<ErrorEntity?> by lazy {
        MutableLiveData<ErrorEntity?>(null)
    }

    val snackbarErrorMessageRes: LiveData<ErrorEntity?> = mSnackbarErrorMessageRes

    fun notifySnackbarMessageShown() {
        mSnackbarErrorMessageRes.value = null
    }

    protected suspend fun <R> handleRepositoryResult(
        result: Result<R>, successHandling: (Result.Success<R>) -> Unit = {}) {
        withContext(Dispatchers.Main) {
            when (result) {
                is Result.Error -> mSnackbarErrorMessageRes.value = result.errorEntity
                is Result.Success -> successHandling(result)
            }
        }
    }
}