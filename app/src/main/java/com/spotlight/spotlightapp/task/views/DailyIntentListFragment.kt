package com.spotlight.spotlightapp.task.views

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.spotlight.spotlightapp.R
import com.spotlight.spotlightapp.data.task.Task
import com.spotlight.spotlightapp.databinding.FragmentDailyIntentListBinding
import com.spotlight.spotlightapp.task.TaskPageRouter
import com.spotlight.spotlightapp.task.adapters.DailyIntentListAdapter
import com.spotlight.spotlightapp.task.viewdata.TaskTransitionName
import com.spotlight.spotlightapp.task.viewmodels.DailyIntentListViewModel
import com.spotlight.spotlightapp.utilities.viewutils.MarginItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DailyIntentListFragment(private val taskPageRouter: TaskPageRouter)
    : Fragment(R.layout.fragment_daily_intent_list),
    DailyIntentListAdapter.Callback {
    private lateinit var viewBinding: FragmentDailyIntentListBinding

    private val viewModel: DailyIntentListViewModel by viewModels()
    private val dailyIntentListAdapter: DailyIntentListAdapter = DailyIntentListAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding = FragmentDailyIntentListBinding.bind(view)
        configureViews()
        observeViewModel()
    }

    override fun onTaskSelected(view: View, task: Task) {
        taskPageRouter.openTaskPage(view, task, false)
    }

    private fun configureViews() {
        configureTasksRecyclerView()
        configureAddButton()
    }

    private fun configureTasksRecyclerView() {
        viewBinding.tasksRecyclerView.apply {
            adapter = dailyIntentListAdapter
            setHasFixedSize(true)
            addItemDecoration(
                MarginItemDecoration(resources, DailyIntentListAdapter.TASK_ITEM_PADDING))
        }
    }

    private fun configureAddButton() {
        viewBinding.addButton.apply {
            setOnClickListener {
                taskPageRouter.openTaskList(viewBinding.addButton)
            }
            transitionName = TaskTransitionName.TASK_LIST.getTransitionName()
        }
    }

    private fun observeViewModel() {
        viewModel.apply {
            dailyIntentList.observe(viewLifecycleOwner) { dailyIntentList ->
                dailyIntentListAdapter.setItems(dailyIntentList)
            }

            willShowEmptyState.observe(viewLifecycleOwner) { willShowEmptyState ->
                viewBinding.willShowEmptyState = willShowEmptyState
                if (!willShowEmptyState) return@observe

                configureEmptyState()
            }
        }
    }

    private fun configureEmptyState() {
        resources.apply {
            val emptyStateTitleTextList = resources.getStringArray(
                R.array.daily_intent_empty_state_title)
            val emptyStateTextListSize = emptyStateTitleTextList.size
            val randomEmptyStateTextIndex = (0 until emptyStateTextListSize).random()

            viewBinding.apply {
                emptyStateTitle.text = emptyStateTitleTextList[randomEmptyStateTextIndex]
                emptyStateSubtitle.text = getStringArray(
                    R.array.daily_intent_empty_state_subtitle)[randomEmptyStateTextIndex]
            }
        }
    }
}