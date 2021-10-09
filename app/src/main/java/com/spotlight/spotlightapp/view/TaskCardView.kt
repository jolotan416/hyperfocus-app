package com.spotlight.spotlightapp.view

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        initialize()
    }

    @ColorInt
    private var cardViewStripColor: Int = ContextCompat.getColor(context, R.color.primaryWhite)
        set(value) {
            viewBinding?.categoryColorStrip?.setBackgroundColor(value)
        }

    private var viewBinding: TaskCardViewBinding? = null

    override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
        viewBinding?.taskContainer?.addView(child)
    }

    fun setCardStripColor(@ColorInt cardStripColor: Int) {
        cardViewStripColor = cardStripColor
    }

    private fun initialize() {
        val inflater = LayoutInflater.from(context)
        viewBinding = TaskCardViewBinding.inflate(inflater, this, true).apply {
            categoryColorStrip.setBackgroundColor(cardViewStripColor)
        }
        radius = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, CARD_RADIUS_DP, resources.displayMetrics)
    }
}