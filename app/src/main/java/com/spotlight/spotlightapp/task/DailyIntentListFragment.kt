package com.spotlight.spotlightapp.task

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.spotlight.spotlightapp.R
import com.spotlight.spotlightapp.databinding.FragmentDailyIntentListBinding

class DailyIntentListFragment : Fragment(R.layout.fragment_daily_intent_list) {
    private lateinit var viewBinding: FragmentDailyIntentListBinding
    private val viewModel: DailyIntentListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding = FragmentDailyIntentListBinding.bind(view)
        configureEmptyState()
        initializeViewModel()
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

    private fun initializeViewModel() {
        viewModel.apply {
            tasks.observe(viewLifecycleOwner) {
                viewBinding.isEmpty = it.isEmpty()
                // TODO: Add recyclerview adapter for daily intent list
            }

            requestTasks()
        }
    }
}