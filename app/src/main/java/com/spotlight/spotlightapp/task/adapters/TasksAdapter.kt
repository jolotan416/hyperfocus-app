package com.spotlight.spotlightapp.task.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.spotlight.spotlightapp.task.CompletedTaskListFragment
import com.spotlight.spotlightapp.task.PendingTaskListFragment
import com.spotlight.spotlightapp.task.TaskListType
import com.spotlight.spotlightapp.task.TasksFragment

class TasksAdapter(fragment: Fragment, private val callback: TasksFragment.Callback)
    : FragmentStateAdapter(fragment) {
    override fun getItemCount() = TaskListType.values().size
    override fun createFragment(position: Int): Fragment =
        when (TaskListType.values()[position]) {
            TaskListType.PENDING -> PendingTaskListFragment(callback)
            TaskListType.COMPLETED -> CompletedTaskListFragment()
        }
}