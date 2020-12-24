package com.spotlight.spotlightapp.task

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

class DailyIntentListFragment : Fragment(R.layout.fragment_daily_intent_list) {
    companion object {
        private const val TASK_ITEM_PADDING = 16f
    }

    private val viewModel: DailyIntentListViewModel by viewModels()
    private lateinit var viewBinding: FragmentDailyIntentListBinding
    private lateinit var dailyIntentListAdapter: DailyIntentListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding = FragmentDailyIntentListBinding.bind(view)
        configureTasksRecyclerView()
        initializeViewModel()
    }

    private fun configureTasksRecyclerView() {
        dailyIntentListAdapter = DailyIntentListAdapter()
        val marginItemDecoration = object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)

                outRect.bottom = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, TASK_ITEM_PADDING, resources.displayMetrics)
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

    private fun initializeViewModel() {
        viewModel.apply {
            tasks.observe(viewLifecycleOwner) { tasks ->
                configureTasks(tasks)
            }

            requestTasks()
        }
    }

    private fun configureTasks(tasks: List<Task>) {
        val hasTasks = tasks.isNotEmpty()
        viewBinding.hasTasks = hasTasks

        if (!hasTasks) {
            configureEmptyState()

            return
        }

        dailyIntentListAdapter.setItems(tasks)
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