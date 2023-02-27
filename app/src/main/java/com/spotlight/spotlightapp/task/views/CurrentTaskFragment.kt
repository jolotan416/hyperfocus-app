package com.spotlight.spotlightapp.task.views

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.spotlight.spotlightapp.task.services.TaskTimerService
import com.spotlight.spotlightapp.task.viewdata.TaskCountDownData
import com.spotlight.spotlightapp.task.viewdata.TaskTransitionName
import com.spotlight.spotlightapp.task.viewmodels.CurrentTaskViewModel
import com.spotlight.spotlightapp.utilities.viewmodelutils.ViewModelErrorListener
import com.spotlight.spotlightapp.utilities.viewmodelutils.viewModelErrorListeners
import com.spotlight.spotlightapp.utilities.viewutils.ComposeTextConfiguration
import com.spotlight.spotlightapp.utilities.viewutils.CustomAlertDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CurrentTaskFragment(private val taskPageRouter: TaskPageRouter) :
    Fragment(R.layout.fragment_current_task) {
    companion object {
        const val TAG = "CurrentTaskFragment"
        const val TASK = "task"
        const val WILL_ALLOW_EDIT = "will_allow_edit"
    }

    private val currentTaskViewModel: CurrentTaskViewModel by viewModels()
    private val viewModelErrorListener: ViewModelErrorListener by viewModelErrorListeners()
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
        viewModelErrorListener.observeErrors(currentTaskViewModel, viewBinding.mainLayout)
        setAlertIntervalResultListener()
        startPostponedEnterTransition()
    }

    private fun configureViews() {
        viewBinding.composeView.setContent {
            CurrentTaskLayout()
        }
    }

    private fun observeViewModel() {
        currentTaskViewModel.currentTaskUIState.observe(viewLifecycleOwner) { uiState ->
            when {
                (uiState.deleteTaskResult != null || uiState.completeTaskResult != null) -> parentFragmentManager.popBackStack()
                else -> {
                    viewBinding.mainLayout.transitionName = viewBinding.mainLayout.transitionName
                        ?: (TaskTransitionName.CURRENT_TASK.getTransitionName(
                            uiState.task.id.toString()))

                    if (uiState.taskCountDownData?.isInitialTaskTimerStart == true) {
                        runTaskTimer(uiState.task)
                    }
                }
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

    private fun runTaskTimer(task: Task) {
        if (task.currentTimerEndDate == null) return

        val intent = Intent(requireContext(), TaskTimerService::class.java).apply {
            action = TaskTimerService.RUN_TASK
            putExtra(TaskTimerService.RUNNING_TASK, task)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requireContext().startForegroundService(intent)
        } else {
            requireContext().startService(intent)
        }
    }

    @Composable
    private fun CurrentTaskLayout() {
        val currentTaskUiState = currentTaskViewModel.currentTaskUIState.observeAsState()

        currentTaskUiState.value?.apply {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.Center) {
                if (taskCountDownData != null && !taskCountDownData!!.isInitialTaskTimerStart) {
                    TaskCountDownTimer(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        countDownData = taskCountDownData!!)
                }

                CurrentTaskView(task = task)
                Spacer(modifier = Modifier.height(28.dp))
                CurrentTaskButtons(task = task, willShowEditButtons = willShowEditButtons)
            }
        }
    }

    @Composable
    private fun TaskCountDownTimer(modifier: Modifier, countDownData: TaskCountDownData) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.Center) {
            val progressBackgroundColor = colorResource(id = R.color.functionGrey)
            val progressForegroundColor = colorResource(id = R.color.functionGreen)
            val startAngle = 270f
            val maxSweepAngle = 360f
            val progressSweepAngle = maxSweepAngle * countDownData.countDownTimerProgress
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .align(Alignment.Center)
                    .padding(20.dp)) {
                drawArc(
                    color = progressBackgroundColor, startAngle = startAngle,
                    sweepAngle = maxSweepAngle, useCenter = false,
                    style = Stroke(16.dp.toPx(), cap = StrokeCap.Round))
                drawArc(
                    color = progressForegroundColor, startAngle = startAngle,
                    sweepAngle = progressSweepAngle, useCenter = false,
                    style = Stroke(16.dp.toPx(), cap = StrokeCap.Round))
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center), text = countDownData.countDownTimerString,
                color = colorResource(id = R.color.primaryBlack), fontSize = 32.sp,
                fontFamily = ComposeTextConfiguration.fontFamily,
                fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        }
    }

    @Composable
    private fun CurrentTaskView(task: Task) {
        Column(
            modifier = Modifier.fillMaxWidth()) {
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
                this.isTimerRunning = task.currentTimerEndDate != null
                configureTimeButton(timeButton, task.alertInterval)
                configureEditButton(editButton, task)
                configureDeleteButton(deleteButton)

                startButton.root.setOnClickListener {
                    currentTaskViewModel.toggleTaskAlertTimer(true)
                }

                doneButton.root.setOnClickListener {
                    currentTaskViewModel.toggleTaskFinished()
                }

                stopButton.root.setOnClickListener {
                    val intent = Intent(requireContext(), TaskTimerService::class.java).apply {
                        action = TaskTimerService.STOP_TASK
                    }
                    requireContext().startService(intent)
                    currentTaskViewModel.toggleTaskAlertTimer(false)
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