package com.spotlight.spotlightapp.task.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.spotlight.spotlightapp.data.task.Task
import com.spotlight.spotlightapp.databinding.NewTaskItemBinding
import com.spotlight.spotlightapp.databinding.PendingTaskListItemBinding
import com.spotlight.spotlightapp.task.viewdata.TaskTransitionName
import com.spotlight.spotlightapp.utilities.viewutils.RecyclerViewType

class PendingTaskListAdapter(private val callback: Callback)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val TASK_ITEM_PADDING = 12f
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
        if (viewType == RecyclerViewType.FOOTER_VIEW_TYPE) NewTaskItemViewHolder(
            NewTaskItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        else TaskItemViewHolder(
            PendingTaskListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TaskItemViewHolder -> holder.bind(
                (asyncListDiffer.currentList[position] as RecyclerViewType.Item<Task>).content)
            is NewTaskItemViewHolder -> holder.bind()
        }
    }

    fun setItems(tasks: List<Task>) {
        asyncListDiffer.submitList(
            tasks.map { RecyclerViewType.Item(it) } + listOf(RecyclerViewType.Footer()))
    }

    inner class TaskItemViewHolder(private val binding: PendingTaskListItemBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            binding.apply {
                this.task = task
                taskPriorityButton.setOnClickListener { callback.selectPendingTask(task) }
                root.transitionName = TaskTransitionName.CURRENT_TASK.getTransitionName(
                    task.id.toString())
                root.setOnClickListener { callback.editTask(root, task) }
            }
        }
    }

    inner class NewTaskItemViewHolder(private val binding: NewTaskItemBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.root.apply {
                transitionName = TaskTransitionName.TASK_FORM.getTransitionName()
                setOnClickListener {
                    callback.createTask(this)
                }
            }
        }
    }

    interface Callback {
        fun createTask(view: View)
        fun editTask(view: View, task: Task)
        fun selectPendingTask(task: Task)
    }
}