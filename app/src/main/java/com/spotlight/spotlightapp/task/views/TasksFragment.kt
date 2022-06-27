package com.spotlight.spotlightapp.task.views

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.spotlight.spotlightapp.R
import com.spotlight.spotlightapp.databinding.FragmentTasksBinding
import com.spotlight.spotlightapp.task.adapters.TasksAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TasksFragment(private val callback: Callback) : Fragment(R.layout.fragment_tasks) {
    companion object {
        const val TAG = "TasksFragment"
    }

    private lateinit var viewBinding: FragmentTasksBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding = FragmentTasksBinding.bind(view)
        configureViewPager()
        startPostponedEnterTransition()
    }

    private fun configureViewPager() {
        viewBinding.viewPager.apply {
            adapter = TasksAdapter(this@TasksFragment, callback)
            isUserInputEnabled = false

            TabLayoutMediator(viewBinding.tabLayout, this) { tab, position ->
                tab.text = getString(TaskListType.values()[position].titleRes)
            }.attach()
        }
    }

    // Abstracts child fragment callbacks from other classes dependent to TasksFragment
    interface Callback : PendingTaskListFragment.Callback
}