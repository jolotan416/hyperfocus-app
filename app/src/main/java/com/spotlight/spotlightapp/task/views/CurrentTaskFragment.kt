package com.spotlight.spotlightapp.task.views

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.*
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.spotlight.spotlightapp.R
import com.spotlight.spotlightapp.data.task.Task
import com.spotlight.spotlightapp.databinding.FragmentCurrentTaskBinding
import com.spotlight.spotlightapp.task.viewmodels.CurrentTaskViewModel
import com.spotlight.spotlightapp.utilities.viewmodelutils.BaseViewModel
import com.spotlight.spotlightapp.utilities.viewmodelutils.ViewModelErrorListener
import com.spotlight.spotlightapp.utilities.viewmodelutils.observeErrors
import com.spotlight.spotlightapp.utilities.viewutils.ComposeTextConfiguration
import com.spotlight.spotlightapp.view.CustomComposeViews
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CurrentTaskFragment() : Fragment(R.layout.fragment_current_task), ViewModelErrorListener {
    companion object {
        const val TAG = "CurrentTaskFragment"
        const val TASK = "task"
        const val WILL_ALLOW_EDIT = "will_allow_edit"
    }

    private val currentTaskViewModel: CurrentTaskViewModel by viewModels()
    private lateinit var viewBinding: FragmentCurrentTaskBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding = FragmentCurrentTaskBinding.bind(view)
        initializeViewModel()
        configureViews()
        startPostponedEnterTransition()
    }

    override val baseViewModel: BaseViewModel
        get() = currentTaskViewModel

    override val snackbarLayout: View
        get() = viewBinding.mainLayout

    private fun initializeViewModel() {
        observeErrors()
        currentTaskViewModel.apply {
            requireArguments().let {
                setCurrentTask(it.getParcelable(TASK)!!, it.getBoolean(WILL_ALLOW_EDIT))
            }

            currentTaskUIState.observe(viewLifecycleOwner) { uiState ->
                when {
                    uiState.completeTaskResult != null -> parentFragmentManager.popBackStack()
                    else -> viewBinding.mainLayout.transitionName = Task::class.java.simpleName + uiState.task.id
                }
            }
        }
    }

    private fun configureViews() {
        viewBinding.composeView.setContent {
            CurrentTaskLayout()
        }
    }

    @Composable
    private fun CurrentTaskLayout() {
        val currentTask = currentTaskViewModel.currentTaskUIState.observeAsState()

        currentTask.value?.apply {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.Center) {
                CurrentTaskView(task = task)
                Spacer(modifier = Modifier.height(56.dp))
                CurrentTaskButtons(willShowEditButtons, task.isFinished)
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
    private fun CurrentTaskButtons(willShowEditButtons: Boolean, isTaskFinished: Boolean) {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {
            when {
                willShowEditButtons -> {
                    CustomComposeViews.Button(
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .fillMaxHeight(),
                        imageRes = R.drawable.ic_edit, labelText = null,
                        buttonColors = ButtonDefaults.buttonColors(
                            backgroundColor = colorResource(id = R.color.functionBlue),
                            contentColor = colorResource(id = R.color.primaryWhite))) {}
                    CustomComposeViews.Button(
                        modifier = Modifier
                            .padding(horizontal = 2.dp)
                            .fillMaxHeight(),
                        imageRes = R.drawable.ic_delete, labelText = null,
                        buttonColors = ButtonDefaults.buttonColors(
                            backgroundColor = colorResource(id = R.color.functionRed),
                            contentColor = colorResource(id = R.color.primaryWhite))) {}
                }
                !isTaskFinished -> {
                    CustomComposeViews.Button(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 4.dp)
                            .fillMaxWidth(),
                        imageRes = 0, labelText = "1 hr",
                        buttonColors = ButtonDefaults.buttonColors(
                            backgroundColor = colorResource(id = R.color.functionBlue),
                            contentColor = colorResource(id = R.color.primaryWhite))) {}
                    CustomComposeViews.Button(
                        modifier = Modifier
                            .padding(start = 2.dp, end = 16.dp)
                            .fillMaxHeight(),
                        imageRes = R.drawable.ic_play, labelText = null,
                        buttonColors = ButtonDefaults.buttonColors(
                            backgroundColor = colorResource(id = R.color.functionBlue),
                            contentColor = colorResource(id = R.color.primaryWhite))) {}
                }
            }

            if (isTaskFinished) {
                CustomComposeViews.Button(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp)
                        .fillMaxWidth(),
                    imageRes = 0, labelText = stringResource(id = R.string.return_to_list),
                    buttonColors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(id = R.color.functionGreen),
                        contentColor = colorResource(id = R.color.primaryWhite))) {
                }
            } else {
                CustomComposeViews.Button(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp)
                        .fillMaxWidth(),
                    imageRes = R.drawable.ic_check, labelText = stringResource(id = R.string.done),
                    buttonColors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(id = R.color.functionGreen),
                        contentColor = colorResource(id = R.color.primaryWhite))) {
                    currentTaskViewModel.completeTask()
                }
            }
        }
    }

    @Preview
    @Composable
    private fun CurrentTaskViewPreview() {
        CurrentTaskView(
            task = Task(
                title = "This is a very long title to check task preview",
                description = "This is a very long task description to check task preview"))
    }

    @Preview
    @Composable
    private fun CurrentTaskEditButtons() {
        CurrentTaskButtons(willShowEditButtons = true, false)
    }

    @Preview
    @Composable
    private fun CurrentTaskNonEditButtons() {
        CurrentTaskButtons(willShowEditButtons = false, false)
    }

    @Preview
    @Composable
    private fun CompletedTaskButtons() {
        CurrentTaskButtons(willShowEditButtons = false, isTaskFinished = true)
    }
}