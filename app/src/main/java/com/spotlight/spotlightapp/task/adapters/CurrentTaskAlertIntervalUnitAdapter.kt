package com.spotlight.spotlightapp.task.adapters

import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.spotlight.spotlightapp.R

class CurrentTaskAlertIntervalUnitAdapter :
    RecyclerView.Adapter<CurrentTaskAlertIntervalUnitAdapter.ViewHolder>() {
    companion object {
        const val ITEM_PADDING = 12f

        private const val ITEM_TEXT_SIZE = 20f
    }

    private val asyncListDiffer = AsyncListDiffer(this, object :
        DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem == newItem
        override fun areContentsTheSame(oldItem: String, newItem: String) = oldItem == newItem
    })

    override fun getItemCount() = asyncListDiffer.currentList.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val textView = TextView(context).apply {
            layoutParams = RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT)
            TextViewCompat.setTextAppearance(this, R.style.bold_overlay_text)
            setTextColor(ContextCompat.getColor(context, R.color.primaryBlack))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, ITEM_TEXT_SIZE)
            gravity = Gravity.CENTER_VERTICAL
        }

        return ViewHolder(textView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(asyncListDiffer.currentList[position])
    }

    fun setItems(items: List<String>) {
        asyncListDiffer.submitList(items)
    }

    class ViewHolder(private val unitTextView: TextView) : RecyclerView.ViewHolder(unitTextView) {
        fun bindData(unitLabel: String) {
            unitTextView.text = unitLabel
        }
    }
}