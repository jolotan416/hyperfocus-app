package com.spotlight.spotlightapp.utilities.viewmodelutils

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.snackbar.Snackbar
import com.spotlight.spotlightapp.R

class ViewModelErrorListener(
    private val context: Context) {
    fun observeErrors(
        errorHolder: ErrorHolder, snackbarLayout: View, lifecycleOwner: LifecycleOwner) {
        errorHolder.snackbarErrorMessageRes.observe(lifecycleOwner) { errorEntity ->
            val snackbarErrorMessage = errorEntity?.let {
                context.getString(it.errorMessageRes, *it.errorMessageArguments)
            } ?: return@observe
            Snackbar.make(snackbarLayout, snackbarErrorMessage, Snackbar.LENGTH_LONG)
                .setBackgroundTint(ContextCompat.getColor(context, R.color.functionRed))
                .setTextColor(ContextCompat.getColor(context, R.color.primaryWhite))
                .show()

            errorHolder.notifySnackbarMessageShown()
        }
    }
}

fun Fragment.viewModelErrorListeners(): Lazy<ViewModelErrorListener> = lazy {
    ViewModelErrorListener(requireContext())
}