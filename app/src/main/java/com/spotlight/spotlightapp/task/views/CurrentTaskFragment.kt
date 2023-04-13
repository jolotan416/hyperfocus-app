package com.spotlight.spotlightapp.task.views

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
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
import com.spotlight.spotlightapp.utilities.permission.PermissionModule
import com.spotlight.spotlightapp.utilities.permission.permissionModule
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
    private val postNotificationsPermissionModule: PermissionModule by permissionModule(
        Manifest.permission.POST_NOTIFICATIONS) {
        currentTaskViewModel.toggleTaskAlertTimer(true)
    }
    private lateinit var viewBinding: FragmentCurrentTaskBinding

    private val onBackPressedCallback: OnBackPressedCallback = object :
        OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            currentTaskViewModel.handleRunningTaskBackPress()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        postNotificationsPermissionModule.initializeModule()
        requireArguments().let {
            currentTaskViewModel.setCurrentTask(
                it.getParcelable(TASK)!!, it.getBoolean(WILL_ALLOW_EDIT))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding = FragmentCurrentTaskBinding.bind(view)
        configureViews()
        configureBackButtonPress()
        observeViewModel()
        viewModelErrorListener.observeErrors(
            currentTaskViewModel, viewBinding.mainLayout, viewLifecycleOwner)
        setAlertIntervalResultListener()
        startPostponedEnterTransition()
    }

    private fun configureViews() {
        viewBinding.composeView.setContent {
            CurrentTaskLayout()
        }
    }

    private fun configureBackButtonPress() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, onBackPressedCallback)
    }

    private fun observeViewModel() {
        currentTaskViewModel.currentTaskUIState.observe(viewLifecycleOwner) { uiState ->
            when {
                (uiState.deleteTaskResult != null || uiState.completeTaskResult != null) -> parentFragmentManager.popBackStack()
                else -> {
                    viewBinding.mainLayout.transitionName = viewBinding.mainLayout.transitionName
                        ?: (TaskTransitionName.CURRENT_TASK.getTransitionName(
                            uiState.task.id.toString()))

                    onBackPressedCallback.isEnabled = uiState.taskCountDownData != null && !uiState.taskCountDownData!!.isInitialTaskTimerStart
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
        if (task.taskTimerData == null) return

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
                CurrentTaskView(task = task)
                Spacer(modifier = Modifier.height(12.dp))
                AnimatedVisibility(
                    visible = taskCountDownData != null && !taskCountDownData!!.isInitialTaskTimerStart) {
                    taskCountDownData?.let { taskCountDownData ->
                        TaskCountDownTimer(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            countDownData = taskCountDownData)
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
                CurrentTaskButtons(task = task, willShowEditButtons = willShowEditButtons)
            }

            if (willShowTimerFinishedDialog) {
                AlertDialog(onDismissRequest = { currentTaskViewModel.onDismissFinishedDialog() },
                            title = {
                                Text(
                                    text = stringResource(
                                        id = R.string.current_task_finished_dialog_title),
                                    color = colorResource(
                                        id = R.color.primaryBlack),
                                    fontFamily = ComposeTextConfiguration.fontFamily,
                                    fontWeight = FontWeight.SemiBold, fontSize = 20.sp)
                            },
                            text = {
                                Text(
                                    text = stringResource(
                                        id = R.string.current_task_finished_dialog_text),
                                    color = colorResource(
                                        id = R.color.primaryBlack),
                                    fontFamily = ComposeTextConfiguration.fontFamily,
                                    fontWeight = FontWeight.Normal, fontSize = 16.sp)
                            },
                            buttons = {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(20.dp),
                                    horizontalArrangement = Arrangement.End) {
                                    Button(
                                        onClick = { currentTaskViewModel.onDismissFinishedDialog() },
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            backgroundColor = colorResource(
                                                id = R.color.functionRed),
                                            contentColor = colorResource(
                                                id = R.color.primaryWhite))) {
                                        Text(
                                            text = stringResource(
                                                id = R.string.current_task_finished_dialog_dismiss_label),
                                            fontFamily = ComposeTextConfiguration.fontFamily,
                                            fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Button(
                                        onClick = {
                                            currentTaskViewModel.toggleTaskFinished()
                                            currentTaskViewModel.onDismissFinishedDialog()
                                        },
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            backgroundColor = colorResource(
                                                id = R.color.functionGreen),
                                            contentColor = colorResource(
                                                id = R.color.primaryWhite))) {
                                        Text(
                                            text = stringResource(
                                                id = R.string.current_task_finished_dialog_confirm_lb),
                                            fontFamily = ComposeTextConfiguration.fontFamily,
                                            fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                                    }
                                }
                            })
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
            val progressBackgroundColor = colorResource(id = R.color.functionLightGrey)
            val progressForegroundColor = colorResource(id = R.color.functionGreen)
            val startAngle = 270f
            val maxSweepAngle = 360f
            val progressSweepAngle: Float by animateFloatAsState(
                targetValue = maxSweepAngle * countDownData.countDownTimerProgress)
            Canvas(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .aspectRatio(1f)
                    .padding(20.dp)
                    .align(Alignment.Center)) {
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
                color = colorResource(id = R.color.primaryBlack), fontSize = 24.sp,
                fontFamily = ComposeTextConfiguration.fontFamily,
                fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        }
    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    private fun CurrentTaskView(task: Task) {
        AnimatedContent(targetState = task.taskTimerData != null) { isTimerRunning ->
            val textAlign = if (isTimerRunning) TextAlign.Center else TextAlign.Left
            Column(
                modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = task.title, color = colorResource(id = R.color.primaryBlack),
                    fontSize = 24.sp, fontFamily = ComposeTextConfiguration.fontFamily,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 28.sp, textAlign = textAlign)
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = task.description, color = colorResource(id = R.color.primaryBlack),
                    fontSize = 16.sp, fontFamily = ComposeTextConfiguration.fontFamily,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 20.sp, textAlign = textAlign)
            }
        }
    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    private fun CurrentTaskButtons(task: Task, willShowEditButtons: Boolean) {
        AnimatedContent(targetState = task.taskTimerData != null) { isTimerRunning ->
            AndroidViewBinding(
                factory = CurrentTaskViewButtonsBinding::inflate,
                modifier = Modifier.fillMaxWidth(),
                update = {
                    this.willShowEditButtons = willShowEditButtons
                    this.isTaskFinished = task.isFinished
                    this.isTimerRunning = isTimerRunning
                    configureTimeButton(timeButton, task.alertInterval)
                    configureEditButton(editButton, task)
                    configureDeleteButton(deleteButton)

                    startButton.root.setOnClickListener {
                        postNotificationsPermissionModule.requestPermissionWithRationale(
                            Manifest.permission.POST_NOTIFICATIONS,
                            CustomAlertDialog.ViewData(
                                title = getString(
                                    R.string.current_task_notification_permission_title),
                                message = getString(
                                    R.string.current_task_notificaiton_permission_message),
                                negativeButtonViewData = CustomAlertDialog.ButtonViewData(
                                    getString(
                                        R.string.current_task_notification_permission_dismiss_label),
                                    ContextCompat.getColor(
                                        requireContext(),
                                        R.color.primaryBlack)) {},
                                positiveButtonViewData = CustomAlertDialog.ButtonViewData(
                                    getString(
                                        R.string.current_task_notification_permission_confirm_label),
                                    ContextCompat.getColor(
                                        requireContext(),
                                        R.color.functionGreen)
                                ) {
                                    postNotificationsPermissionModule.redirectToPermissionSettingsPage()
                                }
                            ))
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