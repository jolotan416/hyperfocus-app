package com.spotlight.spotlightapp.task.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.spotlight.spotlightapp.data.task.Task
import com.spotlight.spotlightapp.databinding.TaskListItemBinding

class TaskListAdapter : RecyclerView.Adapter<TaskListAdapter.ItemViewHolder>() {
    companion object {
        const val TASK_ITEM_PADDING = 12f
    }

    private val asyncListDiffer = AsyncListDiffer(this, object : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Task, newItem: Task) =
            oldItem == newItem
    })

    // TODO: Add support for creating new tasks within the list
    override fun getItemCount() = asyncListDiffer.currentList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ItemViewHolder(
            TaskListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(asyncListDiffer.currentList[position])
    }

    fun setItems(tasks: List<Task>) {
        asyncListDiffer.submitList(tasks)
    }

    class ItemViewHolder(private val binding: TaskListItemBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            binding.task = task
        }
    }
}