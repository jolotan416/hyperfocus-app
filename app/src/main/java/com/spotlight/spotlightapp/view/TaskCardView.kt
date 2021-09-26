package com.spotlight.spotlightapp.view

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorInt
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.spotlight.spotlightapp.R
import com.spotlight.spotlightapp.databinding.TaskCardViewBinding

class TaskCardView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0) : CardView(context, attributeSet, defStyleAttr) {
    companion object {
        private const val CARD_RADIUS_DP = 10f
    }

    init {
        initialize(attributeSet)
    }

    @ColorInt
    var cardViewStripColor: Int = ContextCompat.getColor(context, R.color.primaryWhite)
        set(value) {
            viewBinding?.categoryColorStrip?.setBackgroundColor(value)
        }

    private var viewBinding: TaskCardViewBinding? = null

    fun addTaskView(view: View) {
        viewBinding?.taskContainer?.addView(view)
    }

    private fun initialize(attributeSet: AttributeSet?) {
        retrieveStyleAttributes(attributeSet)
        configureViews()
    }

    private fun retrieveStyleAttributes(attributeSet: AttributeSet?) {
        context.obtainStyledAttributes(attributeSet, R.styleable.TaskCardView).apply {
            cardViewStripColor = getColor(
                R.styleable.TaskCardView_backgroundColor, cardViewStripColor)
            recycle()
        }
    }

    private fun configureViews() {
        val inflater = LayoutInflater.from(context)
        viewBinding = TaskCardViewBinding.inflate(inflater, this, true).apply {
            categoryColorStrip.setBackgroundColor(cardViewStripColor)
        }
        radius = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, CARD_RADIUS_DP, resources.displayMetrics)
    }
}