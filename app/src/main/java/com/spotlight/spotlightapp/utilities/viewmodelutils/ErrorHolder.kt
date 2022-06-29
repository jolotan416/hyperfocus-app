package com.spotlight.spotlightapp.utilities.viewmodelutils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.spotlight.spotlightapp.data.ErrorEntity
import com.spotlight.spotlightapp.data.Result
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ActivityRetainedScoped
class ErrorHolder @Inject constructor() {
    private val mSnackbarErrorMessageRes: MutableLiveData<ErrorEntity?> by lazy {
        MutableLiveData<ErrorEntity?>(null)
    }

    val snackbarErrorMessageRes: LiveData<ErrorEntity?> = mSnackbarErrorMessageRes

    fun notifySnackbarMessageShown() {
        mSnackbarErrorMessageRes.value = null
    }

    suspend fun <R> handleRepositoryResult(
        result: Result<R>, successHandling: (Result.Success<R>) -> Unit = {}) {
        withContext(Dispatchers.Main) {
            when (result) {
                is Result.Error -> mSnackbarErrorMessageRes.value = result.errorEntity
                is Result.Success -> successHandling(result)
            }
        }
    }
}