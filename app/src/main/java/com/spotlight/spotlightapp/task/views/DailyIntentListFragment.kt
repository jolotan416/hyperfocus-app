package com.spotlight.spotlightapp.task.views

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.spotlight.spotlightapp.R
import com.spotlight.spotlightapp.data.task.Task
import com.spotlight.spotlightapp.databinding.FragmentDailyIntentListBinding
import com.spotlight.spotlightapp.task.TaskPageRouter
import com.spotlight.spotlightapp.task.adapters.DailyIntentListAdapter
import com.spotlight.spotlightapp.task.viewmodels.DailyIntentListViewModel
import com.spotlight.spotlightapp.utilities.viewmodelutils.BaseViewModel
import com.spotlight.spotlightapp.utilities.viewmodelutils.ViewModelErrorListener
import com.spotlight.spotlightapp.utilities.viewmodelutils.observeErrors
import com.spotlight.spotlightapp.utilities.viewutils.MarginItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DailyIntentListFragment(private val taskPageRouter: TaskPageRouter)
    : Fragment(R.layout.fragment_daily_intent_list), ViewModelErrorListener,
    DailyIntentListAdapter.Callback {
    private val viewModel: DailyIntentListViewModel by viewModels()
    private lateinit var viewBinding: FragmentDailyIntentListBinding
    private lateinit var dailyIntentListAdapter: DailyIntentListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding = FragmentDailyIntentListBinding.bind(view)
        configureViews()
        observeViewModel()
        observeErrors()
    }

    override val baseViewModel: BaseViewModel
        get() = viewModel

    override val snackbarLayout: View
        get() = viewBinding.root

    override fun onTaskSelected(view: View, task: Task) {
        taskPageRouter.openTaskPage(view, task, false)
    }

    private fun configureViews() {
        configureTasksRecyclerView()
        configureAddButton()
    }

    private fun configureTasksRecyclerView() {
        dailyIntentListAdapter = DailyIntentListAdapter(this)

        viewBinding.taskRecyclerView.apply {
            adapter = dailyIntentListAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addItemDecoration(
                MarginItemDecoration(resources, DailyIntentListAdapter.TASK_ITEM_PADDING))
        }
    }

    private fun configureAddButton() {
        viewBinding.addButton.setOnClickListener {
            taskPageRouter.openTaskList(viewBinding.addButton)
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