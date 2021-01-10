package com.spotlight.spotlightapp.task

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.spotlight.spotlightapp.R
import com.spotlight.spotlightapp.data.task.Task
import com.spotlight.spotlightapp.databinding.FragmentCurrentTaskBinding

class CurrentTaskFragment : Fragment(R.layout.fragment_current_task) {
    companion object {
        const val TAG = "CurrentTaskFragment"
        const val TASK = "task"
    }

    private lateinit var viewBinding: FragmentCurrentTaskBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding = FragmentCurrentTaskBinding.bind(view)
        configureViews(requireArguments().getParcelable(TASK)!!)
        startPostponedEnterTransition()
    }

    private fun configureViews(task: Task) {
        viewBinding.apply {
            this.task = task
            // TODO: Add transition to toolbar
            mainLayout.transitionName = Task::class.java.simpleName + task.id
        }
    }
}