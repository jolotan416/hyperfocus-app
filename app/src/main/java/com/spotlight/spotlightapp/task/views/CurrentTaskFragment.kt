package com.spotlight.spotlightapp.task.views

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.spotlight.spotlightapp.R
import com.spotlight.spotlightapp.data.task.Task
import com.spotlight.spotlightapp.data.task.TaskAlertInterval
import com.spotlight.spotlightapp.databinding.CurrentTaskViewButtonsBinding
import com.spotlight.spotlightapp.databinding.FragmentCurrentTaskBinding
import com.spotlight.spotlightapp.databinding.RoundedButtonBinding
import com.spotlight.spotlightapp.task.TaskPageRouter
import com.spotlight.spotlightapp.task.viewdata.TaskTransitionName
import com.spotlight.spotlightapp.task.viewmodels.CurrentTaskViewModel
import com.spotlight.spotlightapp.utilities.viewmodelutils.ErrorHolder
import com.spotlight.spotlightapp.utilities.viewmodelutils.ViewModelErrorListener
import com.spotlight.spotlightapp.utilities.viewmodelutils.observeErrors
import com.spotlight.spotlightapp.utilities.viewutils.ComposeTextConfiguration
import com.spotlight.spotlightapp.utilities.viewutils.CustomAlertDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CurrentTaskFragment(private val taskPageRouter: TaskPageRouter) :
    Fragment(R.layout.fragment_current_task), ViewModelErrorListener {
    companion object {
        const val TAG = "CurrentTaskFragment"
        const val TASK = "task"
        const val WILL_ALLOW_EDIT = "will_allow_edit"
    }

    @Inject
    lateinit var mErrorHolder: ErrorHolder

    private val currentTaskViewModel: CurrentTaskViewModel by viewModels()
    private lateinit var viewBinding: FragmentCurrentTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireArguments().let {
            currentTaskViewModel.setCurrentTask(
                it.getParcelable(TASK)!!, it.getBoolean(WILL_ALLOW_EDIT))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding = FragmentCurrentTaskBinding.bind(view)
        configureViews()
        observeViewModel()
        observeErrors()
        setAlertIntervalResultListener()
        startPostponedEnterTransition()
    }

    override val errorHolder: ErrorHolder
        get() = mErrorHolder

    override val snackbarLayout: View
        get() = viewBinding.mainLayout

    private fun configureViews() {
        viewBinding.composeView.setContent {
            CurrentTaskLayout()
        }
    }

    private fun observeViewModel() {
        currentTaskViewModel.currentTaskUIState.observe(viewLifecycleOwner) { uiState ->
            when {
                (uiState.deleteTaskResult != null || uiState.completeTaskResult != null) -> parentFragmentManager.popBackStack()
                else -> viewBinding.mainLayout.transitionName = viewBinding.mainLayout.transitionName
                    ?: (TaskTransitionName.CURRENT_TASK.getTransitionName(
                        uiState.task.id.toString()))
            }
        }
    }

    private fun setAlertIntervalResultListener() {
        childFragmentManager.setFragmentResultListener(
            CurrentTaskAlertIntervalDialogFragment.CURRENT_TASK_ALERT_INTERVAL_RESULT,
            viewLifecycleOwner) { requestKey: String, result: Bundle ->
            if (requestKey != CurrentTaskAlertIntervalDialogFragment.CURRENT_TASK_ALERT_INTERVAL_RESULT) return@setFragmentResultListener

            currentTaskViewModel.setCurrentTaskAlertInterval(
                result.getParcelable(
                    CurrentTaskAlertIntervalDialogFragment.CURRENT_TASK_ALERT_INTERVAL)
                    ?: return@setFragmentResultListener)
        }
    }

    @Composable
    private fun CurrentTaskLayout() {
        val currentTask = currentTaskViewModel.currentTaskUIState.observeAsState()

        currentTask.value?.apply {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.Center) {
                CurrentTaskView(task = task)
                Spacer(modifier = Modifier.height(56.dp))
                CurrentTaskButtons(task = task, willShowEditButtons = willShowEditButtons)
            }
        }
    }

    @Composable
    private fun CurrentTaskView(task: Task) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = task.title, color = colorResource(id = R.color.primaryBlack),
                fontSize = 20.sp, fontFamily = ComposeTextConfiguration.fontFamily,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 24.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = task.description, color = colorResource(id = R.color.primaryBlack),
                fontSize = 16.sp, fontFamily = ComposeTextConfiguration.fontFamily,
                fontWeight = FontWeight.Normal,
                lineHeight = 20.sp)
        }
    }

    @Composable
    private fun CurrentTaskButtons(task: Task, willShowEditButtons: Boolean) {
        AndroidViewBinding(
            factory = CurrentTaskViewButtonsBinding::inflate, modifier = Modifier.fillMaxWidth(),
            update = {
                this.willShowEditButtons = willShowEditButtons
                this.isTaskFinished = task.isFinished
                configureTimeButton(timeButton, task.alertInterval)
                configureEditButton(editButton, task)
                configureDeleteButton(deleteButton)

                startButton.root.setOnClickListener {
                    currentTaskViewModel.toggleTaskAlertTimer(true)
                }

                doneButton.root.setOnClickListener {
                    currentTaskViewModel.toggleTaskFinished()
                }
            })
    }

    private fun configureTimeButton(
        timeButton: RoundedButtonBinding, taskAlertInterval: TaskAlertInterval) {
        timeButton.apply {
            buttonText = "${taskAlertInterval.amount} ${
                resources.getQuantityString(
                    taskAlertInterval.unit.labelPluralRes, taskAlertInterval.amount)
            }"

            root.setOnClickListener {
                CurrentTaskAlertIntervalDialogFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(
                            CurrentTaskAlertIntervalDialogFragment.CURRENT_TASK_ALERT_INTERVAL,
                            taskAlertInterval)
                    }
                }.show(childFragmentManager, CurrentTaskAlertIntervalDialogFragment.TAG)
            }
        }
    }

    private fun configureEditButton(editButton: RoundedButtonBinding, task: Task) {
        editButton.root.apply {
            transitionName = TaskTransitionName.TASK_FORM.getTransitionName(
                task.id.toString())
            setOnClickListener {
                taskPageRouter.openTaskForm(this, task)
            }
        }
    }

    private fun configureDeleteButton(deleteButton: RoundedButtonBinding) {
        deleteButton.root.setOnClickListener {
            val customAlertDialogViewData = CustomAlertDialog.ViewData(
                title = getString(R.string.delete_task_dialog_title),
                negativeButtonViewData = CustomAlertDialog.ButtonViewData(
                    getString(R.string.no), ContextCompat.getColor(
                        requireContext(), R.color.primaryBlack)) {},
                positiveButtonViewData = CustomAlertDialog.ButtonViewData(
                    getString(R.string.yes), ContextCompat.getColor(
                        requireContext(),
                        R.color.functionGreen)) { currentTaskViewModel.deleteTask() }
            )

            CustomAlertDialog(requireContext(), customAlertDialogViewData).show()
        }
    }
}