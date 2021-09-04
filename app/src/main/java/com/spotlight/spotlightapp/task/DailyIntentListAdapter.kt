package com.spotlight.spotlightapp.task

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.spotlight.spotlightapp.R
import com.spotlight.spotlightapp.data.task.Task
import com.spotlight.spotlightapp.databinding.DailyIntentListItemBinding

class DailyIntentListAdapter(private val callback: Callback)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val TASK_ITEM_PADDING = 12f

        private const val ITEM_VIEW_TYPE = 0
        private const val TITLE_VIEW_TYPE = 1

        private const val TITLE_TEXT_SIZE = 20f
        private const val TITLE_BOTTOM_PADDING = 16f
    }

    private val asyncListDiffer = AsyncListDiffer(this, object : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Task, newItem: Task) = oldItem == newItem
    })

    override fun getItemCount() = asyncListDiffer.currentList.size + 1
    override fun getItemViewType(position: Int) =
        if (position == 0) TITLE_VIEW_TYPE else ITEM_VIEW_TYPE

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        if (viewType == TITLE_VIEW_TYPE) {
            TitleViewHolder(TextView(parent.context))
        } else {
            val binding = DailyIntentListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)

            ItemViewHolder(binding)
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TitleViewHolder -> holder.configure()
            is ItemViewHolder -> holder.bindData(asyncListDiffer.currentList[position - 1])
        }
    }

    fun setItems(tasks: List<Task>) {
        asyncListDiffer.submitList(tasks)
    }

    inner class TitleViewHolder(private val textView: TextView) :
        RecyclerView.ViewHolder(textView) {
        fun configure() {
            textView.apply {
                TextViewCompat.setTextAppearance(this, R.style.overlay_text)

                setText(R.string.daily_intent_list_title)
                textSize = TITLE_TEXT_SIZE
                typeface = ResourcesCompat.getFont(context, R.font.lato_medium)

                val bottomPadding = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, TITLE_BOTTOM_PADDING, resources.displayMetrics)
                    .toInt()
                setPadding(paddingLeft, paddingTop, paddingRight, bottomPadding)
            }
        }
    }

    inner class ItemViewHolder(private val binding: DailyIntentListItemBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bindData(task: Task) {
            binding.apply {
                this.task = task

                root.apply {
                    transitionName = Task::class.java.simpleName + task.id
                    setOnClickListener {
                        callback.onTaskSelected(this, task)
                    }
                }
            }
        }
    }

    interface Callback {
        fun onTaskSelected(view: View, task: Task)
    }
}