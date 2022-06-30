package com.spotlight.spotlightapp.task.adapters

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
import com.spotlight.spotlightapp.utilities.viewutils.RecyclerViewType

class DailyIntentListAdapter(private val callback: Callback)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val TASK_ITEM_PADDING = 12f

        private const val TITLE_TEXT_SIZE = 16f
        private const val TITLE_BOTTOM_PADDING = 8f
    }

    private val asyncListDiffer = AsyncListDiffer(
        this, object : DiffUtil.ItemCallback<RecyclerViewType<Task>>() {
            override fun areItemsTheSame(
                oldItem: RecyclerViewType<Task>, newItem: RecyclerViewType<Task>) =
                oldItem.isEqual(newItem) { firstItem, secondItem -> firstItem.id == secondItem?.id }

            override fun areContentsTheSame(
                oldItem: RecyclerViewType<Task>, newItem: RecyclerViewType<Task>) =
                oldItem.isEqual(newItem) { firstItem, secondItem -> firstItem == secondItem }
        })

    override fun getItemCount() = asyncListDiffer.currentList.size
    override fun getItemViewType(position: Int) = asyncListDiffer.currentList[position].viewTypeInt

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        if (viewType == RecyclerViewType.HEADER_VIEW_TYPE) {
            TitleViewHolder(TextView(parent.context))
        } else {
            ItemViewHolder(
                DailyIntentListItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false))
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TitleViewHolder -> holder.configure()
            is ItemViewHolder -> holder.bindData(
                (asyncListDiffer.currentList[position] as RecyclerViewType.Item<Task>).content)
        }
    }

    fun setItems(tasks: List<Task>) {
        asyncListDiffer.submitList(listOf(RecyclerViewType.Header<Task>()) + tasks.map {
            RecyclerViewType.Item(it)
        })
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

    inner class ItemViewHolder(private val taskBinding: DailyIntentListItemBinding)
        : RecyclerView.ViewHolder(taskBinding.root) {
        fun bindData(task: Task) {
            taskBinding.apply {
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