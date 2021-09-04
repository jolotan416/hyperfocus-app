package com.spotlight.spotlightapp.task

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.spotlight.spotlightapp.R
import com.spotlight.spotlightapp.databinding.FragmentTaskListBinding

class TaskListFragment : Fragment(R.layout.fragment_task_list) {
    companion object {
        const val TAG = "TaskListFragment"
    }

    private lateinit var viewBinding: FragmentTaskListBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding = FragmentTaskListBinding.bind(view)
    }
}