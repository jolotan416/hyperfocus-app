package com.spotlight.spotlightapp.utilities.viewutils

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.spotlight.spotlightapp.R
import com.spotlight.spotlightapp.utilities.viewmodelutils.BaseViewModel

abstract class BaseFragment(@LayoutRes fragmentLayoutRes: Int) : Fragment(fragmentLayoutRes) {
    protected abstract val viewModel: BaseViewModel
    protected abstract val snackbarLayout: ViewGroup

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.snackbarErrorMessageRes.observe(viewLifecycleOwner) { errorEntity ->
            val snackbarErrorMessage = errorEntity?.let {
                requireContext().getString(it.errorMessageRes, *it.errorMessageArguments)
            } ?: return@observe
            Snackbar.make(snackbarLayout, snackbarErrorMessage, Snackbar.LENGTH_LONG)
                .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.functionRed))
                .setTextColor(ContextCompat.getColor(requireContext(), R.color.primaryWhite))
                .show()

            viewModel.notifySnackbarMessageShown()
        }
    }
}