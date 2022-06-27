package com.spotlight.spotlightapp.task.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.spotlight.spotlightapp.task.TaskPageRouter
import com.spotlight.spotlightapp.task.views.CompletedTaskListFragment
import com.spotlight.spotlightapp.task.views.PendingTaskListFragment
import com.spotlight.spotlightapp.task.views.TaskListType

class TasksAdapter(fragment: Fragment, private val taskPageRouter: TaskPageRouter)
    : FragmentStateAdapter(fragment) {
    override fun getItemCount() = TaskListType.values().size
    override fun createFragment(position: Int): Fragment =
        when (TaskListType.values()[position]) {
            TaskListType.PENDING -> PendingTaskListFragment(taskPageRouter)
            TaskListType.COMPLETED -> CompletedTaskListFragment()
        }
}