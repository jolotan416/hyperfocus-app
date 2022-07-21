package com.spotlight.spotlightapp.task.views

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.doOnNextLayout
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.spotlight.spotlightapp.R
import com.spotlight.spotlightapp.databinding.FragmentCurrentTaskAlertIntervialDialogBinding
import com.spotlight.spotlightapp.task.adapters.CurrentTaskAlertIntervalUnitAdapter
import com.spotlight.spotlightapp.data.task.TaskAlertInterval
import com.spotlight.spotlightapp.task.viewmodels.CurrentTaskAlertIntervalViewModel
import com.spotlight.spotlightapp.utilities.viewutils.dpToPx

class CurrentTaskAlertIntervalDialogFragment :
    DialogFragment(R.layout.fragment_current_task_alert_intervial_dialog) {
    companion object {
        const val TAG = "CurrentTaskAlertIntervalDialogFragment"
        const val CURRENT_TASK_ALERT_INTERVAL = "current_task_alert_interval"
        const val CURRENT_TASK_ALERT_INTERVAL_RESULT = "current_task_alert_interval_result"
    }

    private lateinit var viewBinding: FragmentCurrentTaskAlertIntervialDialogBinding

    private val currentTaskAlertIntervalViewModel: CurrentTaskAlertIntervalViewModel by viewModels()
    private val currentTaskAlertIntervalUnitAdapter = CurrentTaskAlertIntervalUnitAdapter()
    private var isUnitScrollUserInput = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        currentTaskAlertIntervalViewModel.setCurrentTaskAlertInterval(
            requireArguments().getParcelable(CURRENT_TASK_ALERT_INTERVAL)!!)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        viewBinding = FragmentCurrentTaskAlertIntervialDialogBinding.inflate(layoutInflater)

        return AlertDialog.Builder(requireContext())
            .setView(viewBinding.root)
            .create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureViews()
        observeViewModel()
    }

    private fun configureViews() {
        configureAmountEditText()
        configureUnitViewPager()
        configureButtons()
    }

    private fun configureAmountEditText() {
        viewBinding.alertIntervalAmountEditText.doAfterTextChanged {
            currentTaskAlertIntervalViewModel.setAlertAmount(it?.toString() ?: "")
        }
    }

    private fun configureUnitViewPager() {
        viewBinding.alertIntervalUnitViewPager.apply {
            adapter = currentTaskAlertIntervalUnitAdapter
            offscreenPageLimit = 1
            (getChildAt(0) as RecyclerView).apply {
                val paddingVertical = viewBinding.alertIntervalAmountEditText.paddingTop -
                        CurrentTaskAlertIntervalUnitAdapter.ITEM_PADDING.dpToPx(resources)
                setPadding(0, paddingVertical, 0, paddingVertical)
                clipToPadding = false
                overScrollMode = View.OVER_SCROLL_NEVER
            }
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrollStateChanged(state: Int) {
                    if (state == RecyclerView.SCROLL_STATE_DRAGGING) {
                        isUnitScrollUserInput = true
                    }
                }

                override fun onPageSelected(position: Int) {
                    if (!isUnitScrollUserInput) return

                    currentTaskAlertIntervalViewModel.setAlertIntervalUnit(
                        TaskAlertInterval.Unit.values()[position])
                }
            })
        }
    }

    private fun configureButtons() {
        viewBinding.apply {
            cancelButton.root.setOnClickListener { dismiss() }
            setButton.root.setOnClickListener {
                setFragmentResult(CURRENT_TASK_ALERT_INTERVAL_RESULT, Bundle().apply {
                    putParcelable(
                        CURRENT_TASK_ALERT_INTERVAL,
                        currentTaskAlertIntervalViewModel.currentTaskAlertInterval.value!!)
                })
                dismiss()
            }
        }
    }

    private fun observeViewModel() {
        currentTaskAlertIntervalViewModel.currentTaskAlertInterval.observe(
            viewLifecycleOwner) { currentTaskAlertInterval ->
            updateAlertIntervalAmountEditText(currentTaskAlertInterval.amount)
            updateSetButton(currentTaskAlertInterval.amount != 0)
            updateAlertIntervalUnitList(
                currentTaskAlertInterval.amount, currentTaskAlertInterval.unit)
        }
    }

    private fun updateAlertIntervalAmountEditText(amount: Int) {
        viewBinding.alertIntervalAmountEditText.apply {
            if (text.toString() != amount.toString()) {
                setText(amount.toString())
                setSelection(amount.toString().length)
            }
        }
    }

    private fun updateSetButton(isEnabled: Boolean) {
        viewBinding.setButton.apply {
            root.isEnabled = isEnabled
            backgroundTint = ContextCompat.getColor(
                requireContext(),
                if (isEnabled) R.color.functionGreen else R.color.functionGrey)
        }
    }

    private fun updateAlertIntervalUnitList(amount: Int, unit: TaskAlertInterval.Unit) {
        val unitPosition = TaskAlertInterval.Unit.getPosition(unit)
        isUnitScrollUserInput = false

        currentTaskAlertIntervalUnitAdapter.setItems(
            TaskAlertInterval.Unit.getLabelList(resources, amount))
        viewBinding.alertIntervalUnitViewPager.apply {
            doOnNextLayout {
                currentItem = unitPosition
            }
        }
    }
}