package com.spotlight.spotlightapp.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import com.spotlight.spotlightapp.databinding.AppToolbarBinding
import com.spotlight.spotlightapp.utilities.BindingAdapters

class AppToolbar @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0, defStyleRes: Int = 0) :
    LinearLayout(context, attributeSet, defStyleAttr, defStyleRes) {
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

    private fun initialize() {
        orientation = HORIZONTAL

        viewBinding = AppToolbarBinding.inflate(LayoutInflater.from(context), this)
        viewBinding.backButton.setOnClickListener {
            (context as? AppCompatActivity)?.onBackPressed()
        }
    }
}