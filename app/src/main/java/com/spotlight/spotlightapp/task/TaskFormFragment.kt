package com.spotlight.spotlightapp.task

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.spotlight.spotlightapp.R
import com.spotlight.spotlightapp.databinding.FragmentTaskFormBinding

class TaskFormFragment : Fragment(R.layout.fragment_task_form) {
    companion object {
        const val TAG = "TaskFormFragment"

        const val TASK = "task"
    }

    private lateinit var binding: FragmentTaskFormBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentTaskFormBinding.bind(view)
    }
}