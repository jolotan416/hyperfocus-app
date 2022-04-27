package com.spotlight.spotlightapp.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.spotlight.spotlightapp.databinding.AppToolbarBinding
import com.spotlight.spotlightapp.utilities.BindingAdapters
import dagger.hilt.android.internal.managers.ViewComponentManager

class AppToolbar @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0, defStyleRes: Int = 0) :
    ConstraintLayout(context, attributeSet, defStyleAttr, defStyleRes) {
    private lateinit var viewBinding: AppToolbarBinding

    init {
        initialize()
    }

    fun setToolbarBackgroundColor(@ColorInt backgroundColor: Int) {
        background = ColorDrawable(backgroundColor)
    }

    fun setComponentTint(@ColorInt componentTint: Int) {
        viewBinding.apply {
            backButton.imageTintList = ColorStateList.valueOf(componentTint)
            titleTextView.setTextColor(componentTint)
        }
    }

    fun setBackButtonIcon(backButtonIcon: Drawable) {
        viewBinding.backButton.apply {
            BindingAdapters.toggleVisibility(this, true)
            setImageDrawable(backButtonIcon)
        }
    }

    fun setTitleText(titleText: String) {
        viewBinding.titleTextView.text = titleText
    }

    fun setHasDivider(hasDivider: Boolean) {
        BindingAdapters.toggleVisibility(viewBinding.divider, hasDivider)
    }

    fun setActionButtonText(actionButtonText: String) {
        viewBinding.actionButton.apply {
            BindingAdapters.toggleVisibility(this, true)
            text = actionButtonText
        }
    }

    fun setActionButtonEnabled(isActionButtonEnabled: Boolean) {
        viewBinding.actionButton.isEnabled = isActionButtonEnabled
    }

    fun setActionButtonTextClickListener(listener: View.OnClickListener) {
        viewBinding.actionButton.setOnClickListener(listener)
    }

    private fun initialize() {
        viewBinding = AppToolbarBinding.inflate(LayoutInflater.from(context), this)
        viewBinding.backButton.setOnClickListener {
            when (val retrievedContext = context) {
                is AppCompatActivity -> retrievedContext.onBackPressed()
                is ViewComponentManager.FragmentContextWrapper ->
                    (retrievedContext.baseContext as? AppCompatActivity)?.onBackPressed()
            }
        }
    }
}