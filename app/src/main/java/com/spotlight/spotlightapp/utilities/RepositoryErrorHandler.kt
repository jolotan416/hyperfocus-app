package com.spotlight.spotlightapp.utilities

import com.spotlight.spotlightapp.data.ErrorEntity
import com.spotlight.spotlightapp.data.Result
import javax.inject.Inject

class RepositoryErrorHandler @Inject constructor() {
    suspend fun <T> handleGeneralRepositoryOperation(
        operation: suspend () -> Result<T>): Result<T> =
        try {
            operation()
        } catch (exception: Exception) {
            Result.Error(ErrorEntity.GENERAL_ERROR_ENTITY)
        }
}