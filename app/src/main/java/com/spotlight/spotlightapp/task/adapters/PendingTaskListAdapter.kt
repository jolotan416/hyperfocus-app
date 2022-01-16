package com.spotlight.spotlightapp.task.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.spotlight.spotlightapp.data.task.Task
import com.spotlight.spotlightapp.databinding.NewTaskItemBinding
import com.spotlight.spotlightapp.databinding.PendingTaskListItemBinding

class PendingTaskListAdapter(private val callback: PendingTaskListCallback)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val TASK_ITEM_PADDING = 12f

        private const val TASK_ITEM_TYPE = 0
        private const val NEW_TASK_CTA_ITEM_TYPE = 1
    }

    private val asyncListDiffer = AsyncListDiffer(this, object : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Task, newItem: Task) =
            oldItem.title == newItem.title &&
                    oldItem.description == newItem.description &&
                    oldItem.priority == newItem.priority &&
                    oldItem.category == newItem.category &&
                    oldItem.isFinished == newItem.isFinished
    })

    override fun getItemCount() = asyncListDiffer.currentList.size + 1

    override fun getItemViewType(position: Int) =
        if (position in asyncListDiffer.currentList.indices) TASK_ITEM_TYPE
        else NEW_TASK_CTA_ITEM_TYPE

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        if (viewType == TASK_ITEM_TYPE) TaskItemViewHolder(
            PendingTaskListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        else NewTaskItemViewHolder(
            NewTaskItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TaskItemViewHolder -> holder.bind(asyncListDiffer.currentList[position])
            is NewTaskItemViewHolder -> holder.bind()
        }
    }

    fun setItems(tasks: List<Task>) {
        asyncListDiffer.submitList(tasks)
    }

    inner class TaskItemViewHolder(private val binding: PendingTaskListItemBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            binding.apply {
                this.task = task
                taskPriorityButton.setOnClickListener { callback.selectPendingTask(task) }
                root.setOnClickListener { callback.editTask(task) }
            }
        }
    }

    inner class NewTaskItemViewHolder(private val binding: NewTaskItemBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.root.setOnClickListener {
                callback.createTask()
            }
        }
    }

    interface PendingTaskListCallback {
        fun createTask()
        fun editTask(task: Task)
        fun selectPendingTask(task: Task)
    }
}