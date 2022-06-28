package com.spotlight.spotlightapp.utilities.viewmodelutils

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.spotlight.spotlightapp.R

interface ViewModelErrorListener {
    val baseViewModel: BaseViewModel
    val snackbarLayout: View
}

fun ViewModelErrorListener.observeErrors() {
    val (lifecycleOwner, context) = when (this) {
        is AppCompatActivity -> Pair(this, this)
        is Fragment -> Pair(viewLifecycleOwner, requireContext())
        else -> throw IllegalStateException(
            "ViewModelErrorListener can only be either an instance of  AppCompatActivity or Fragment")
    }

    baseViewModel.snackbarErrorMessageRes.observe(lifecycleOwner) { errorEntity ->
        val snackbarErrorMessage = errorEntity?.let {
            context.getString(it.errorMessageRes, *it.arguments)
        } ?: return@observe
        Snackbar.make(snackbarLayout, snackbarErrorMessage, Snackbar.LENGTH_LONG)
            .setBackgroundTint(ContextCompat.getColor(context, R.color.primaryWhite))
            .setTextColor(ContextCompat.getColor(context, R.color.primaryBlack))
            .show()

        baseViewModel.notifySnackbarMessageShown()
    }
}