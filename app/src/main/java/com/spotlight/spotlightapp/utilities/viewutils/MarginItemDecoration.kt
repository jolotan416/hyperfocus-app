package com.spotlight.spotlightapp.utilities.viewutils

import android.content.res.Resources
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginItemDecoration(private val resources: Resources, private val itemPadding: Float) :
    RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        outRect.bottom = itemPadding.dpToPx(resources)
    }
}