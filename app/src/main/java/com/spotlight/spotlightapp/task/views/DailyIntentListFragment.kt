package com.spotlight.spotlightapp.task.views

import android.graphics.Rect
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.spotlight.spotlightapp.R
import com.spotlight.spotlightapp.data.task.Task
import com.spotlight.spotlightapp.databinding.FragmentDailyIntentListBinding
import com.spotlight.spotlightapp.task.TaskPageRouter
import com.spotlight.spotlightapp.task.adapters.DailyIntentListAdapter
import com.spotlight.spotlightapp.task.viewmodels.DailyIntentListViewModel
import com.spotlight.spotlightapp.utilities.BaseViewModel
import com.spotlight.spotlightapp.utilities.ViewModelErrorListener
import com.spotlight.spotlightapp.utilities.observeErrors
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
        val marginItemDecoration = object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)

                outRect.bottom = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, DailyIntentListAdapter.TASK_ITEM_PADDING,
                    resources.displayMetrics)
                    .toInt()
            }
        }

        viewBinding.taskRecyclerView.apply {
            adapter = dailyIntentListAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addItemDecoration(marginItemDecoration)
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