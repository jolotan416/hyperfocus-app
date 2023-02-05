package com.spotlight.spotlightapp.utilities.viewmodelutils

import android.content.Context
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.snackbar.Snackbar
import com.spotlight.spotlightapp.R

class ViewModelErrorListenerDelegate(
    private val context: Context, private val lifecycleOwner: LifecycleOwner) {
    fun observeErrors(errorHolderDelegate: ErrorHolderDelegate, snackbarLayout: ViewGroup) {
        errorHolderDelegate.snackbarErrorMessageRes.observe(lifecycleOwner) { errorEntity ->
            val snackbarErrorMessage = errorEntity?.let {
                context.getString(it.errorMessageRes, *it.errorMessageArguments)
            } ?: return@observe
            Snackbar.make(snackbarLayout, snackbarErrorMessage, Snackbar.LENGTH_LONG)
                .setBackgroundTint(ContextCompat.getColor(context, R.color.functionRed))
                .setTextColor(ContextCompat.getColor(context, R.color.primaryWhite))
                .show()

            errorHolderDelegate.notifySnackbarMessageShown()
        }
    }
}

fun Fragment.viewModelErrorListener(): Lazy<ViewModelErrorListenerDelegate> =
    lazy { ViewModelErrorListenerDelegate(requireContext(), viewLifecycleOwner) }