package com.spotlight.spotlightapp.task.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.spotlight.spotlightapp.task.TaskListType
import kotlin.reflect.full.createInstance

class TasksAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = TaskListType.values().size
    override fun createFragment(position: Int) =
        TaskListType.values()[position].fragmentClass.createInstance()
}