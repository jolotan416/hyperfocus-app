package com.spotlight.spotlightapp.task

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.spotlight.spotlightapp.R
import com.spotlight.spotlightapp.databinding.FragmentDailyIntentListBinding

class DailyIntentListFragment : Fragment(R.layout.fragment_daily_intent_list) {
    private lateinit var viewBinding: FragmentDailyIntentListBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding = FragmentDailyIntentListBinding.bind(view)
        configureEmptyState()
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