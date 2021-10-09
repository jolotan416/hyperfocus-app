package com.spotlight.spotlightapp.task

import android.graphics.Rect
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.spotlight.spotlightapp.R
import com.spotlight.spotlightapp.data.task.Task
import com.spotlight.spotlightapp.databinding.FragmentDailyIntentListBinding
import com.spotlight.spotlightapp.task.adapters.DailyIntentListAdapter

class DailyIntentListFragment(
    private val callback: Callback)
    : Fragment(R.layout.fragment_daily_intent_list), DailyIntentListAdapter.Callback {
    private val viewModel: TaskListViewModel by activityViewModels()
    private lateinit var viewBinding: FragmentDailyIntentListBinding
    private lateinit var dailyIntentListAdapter: DailyIntentListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.requestTasks()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding = FragmentDailyIntentListBinding.bind(view)
        configureViews()
        observeViewModel()
    }

    override fun onTaskSelected(view: View, task: Task) {
        callback.openTaskPage(view, task)
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
            callback.openTaskList()
        }
    }

    private fun observeViewModel() {
        viewModel.dailyIntentList.observe(viewLifecycleOwner) { dailyIntentList ->
            val willShowEmptyState = dailyIntentList.isNullOrEmpty()
            viewBinding.willShowEmptyState = willShowEmptyState
            if (willShowEmptyState) {
                configureEmptyState()
            } else {
                dailyIntentListAdapter.setItems(dailyIntentList)
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

    interface Callback {
        fun openTaskPage(view: View, task: Task)
        fun openTaskList()
    }
}