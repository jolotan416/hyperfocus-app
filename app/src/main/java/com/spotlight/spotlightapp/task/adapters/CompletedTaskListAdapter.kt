package com.spotlight.spotlightapp.task.adapters

import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.spotlight.spotlightapp.R
import com.spotlight.spotlightapp.data.task.Task
import com.spotlight.spotlightapp.task.viewdata.TaskTransitionName
import com.spotlight.spotlightapp.utilities.viewutils.ComposeTextConfiguration

class CompletedTaskListAdapter(private val callback: Callback) :
    RecyclerView.Adapter<CompletedTaskListAdapter.ViewHolder>() {
    companion object {
        const val ITEM_PADDING = 12f
    }

    private val asyncListDiffer = AsyncListDiffer(this, object : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Task, newItem: Task) =
            oldItem == newItem
    })

    override fun getItemCount(): Int = asyncListDiffer.currentList.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val composeView = ComposeView(parent.context).apply {
            layoutParams = RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT)
        }

        return ViewHolder(composeView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(asyncListDiffer.currentList[position])
    }

    fun setItems(tasks: List<Task>) {
        asyncListDiffer.submitList(tasks)
    }

    inner class ViewHolder(private val composeView: ComposeView) :
        RecyclerView.ViewHolder(composeView) {
        fun bindData(task: Task) {
            composeView.apply {
                transitionName = TaskTransitionName.CURRENT_TASK.getTransitionName(
                    task.id.toString())
                setContent {
                    CompletedTaskListCard(task = task)
                }
            }
        }

        @Composable
        private fun CompletedTaskListCard(task: Task) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { callback.onSelectCompletedTask(composeView, task) },
                shape = RoundedCornerShape(10.dp),
                backgroundColor = colorResource(id = R.color.primaryWhite),
                elevation = 0.dp) {
                Text(
                    text = task.title, color = colorResource(id = R.color.primaryBlack),
                    fontSize = 20.sp, fontFamily = ComposeTextConfiguration.fontFamily,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 32.sp, maxLines = 2, overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(12.dp))
            }
        }
    }

    interface Callback {
        fun onSelectCompletedTask(view: View, task: Task)
    }
}