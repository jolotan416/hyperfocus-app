package com.spotlight.spotlightapp.utilities.viewutils

import android.content.Context
import android.view.LayoutInflater
import androidx.annotation.ColorInt
import androidx.appcompat.app.AlertDialog
import com.spotlight.spotlightapp.databinding.CustomAlertDialogBinding

class CustomAlertDialog(context: Context, viewData: ViewData) {
    private var alertDialog: AlertDialog

    init {
        val inflater = LayoutInflater.from(context)
        val customAlertDialogBinding = CustomAlertDialogBinding.inflate(inflater)
        alertDialog = AlertDialog.Builder(context)
            .setView(customAlertDialogBinding.root)
            .create()

        customAlertDialogBinding.apply {
            customViewData = viewData
            negativeButtonTextView.setOnClickListener {
                viewData.negativeButtonViewData.onClickListener()

                alertDialog.dismiss()
            }

            viewData.positiveButtonViewData?.apply {
                positiveButtonTextView.setOnClickListener {
                    onClickListener()

                    alertDialog.dismiss()
                }
            }
        }
    }

    fun show() {
        alertDialog.show()
    }

    data class ViewData(
        val title: String,
        val negativeButtonViewData: ButtonViewData,
        val message: String? = null,
        val positiveButtonViewData: ButtonViewData? = null
    ) {
        val hasMessage: Boolean
            get() = message != null

        val hasPositiveButton: Boolean
            get() = positiveButtonViewData != null
    }

    data class ButtonViewData(
        val buttonText: String,
        @ColorInt val buttonColor: Int,
        val onClickListener: () -> Unit
    )
}