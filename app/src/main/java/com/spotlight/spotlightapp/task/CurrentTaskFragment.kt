package com.spotlight.spotlightapp.task

import android.os.Bundle
import android.view.View
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
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
import com.spotlight.spotlightapp.utilities.TextConfiguration

class CurrentTaskFragment : Fragment(R.layout.fragment_current_task) {
    companion object {
        const val TAG = "CurrentTaskFragment"
        const val TASK = "task"
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

    private fun initializeViewModel() {
        currentTaskViewModel.apply {
            setCurrentTask(requireArguments().getParcelable(TASK)!!)

            currentTask.observe(viewLifecycleOwner) { currentTask ->
                viewBinding.apply {
                    this.task = task
                    mainLayout.transitionName = Task::class.java.simpleName + currentTask.id
                }
            }
        }
    }

    private fun configureViews() {
        viewBinding.composeView.setContent {
            CurrentTaskLayout()
        }
    }

    @Preview
    @Composable
    private fun CurrentTaskLayout() {
        val currentTask = currentTaskViewModel.currentTask.observeAsState()

        currentTask.value?.also {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.Center) {
                CurrentTaskView(task = it)
                Spacer(modifier = Modifier.height(56.dp))
                CurrentTaskButtons()
            }
        }
    }

    @Composable
    private fun CurrentTaskView(task: Task) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = task.title, color = colorResource(id = R.color.primaryBlack),
                fontSize = 20.sp, fontFamily = TextConfiguration.fontFamily,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 24.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = task.description, color = colorResource(id = R.color.primaryBlack),
                fontSize = 16.sp, fontFamily = TextConfiguration.fontFamily,
                fontWeight = FontWeight.Normal,
                lineHeight = 20.sp)
        }
    }

    @Preview
    @Composable
    private fun CurrentTaskButtons() {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 4.dp),
                contentAlignment = Alignment.Center) {
                CurrentTaskButton(
                    imageRes = R.drawable.ic_bell, labelText = "1 hr",
                    buttonColors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(id = R.color.functionOrange),
                        contentColor = colorResource(id = R.color.primaryWhite))) {}
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 2.dp),
                contentAlignment = Alignment.Center) {
                CurrentTaskButton(
                    imageRes = R.drawable.ic_play, labelText = stringResource(id = R.string.start),
                    buttonColors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(id = R.color.functionBlue),
                        contentColor = colorResource(id = R.color.primaryWhite))) {}
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 4.dp),
                contentAlignment = Alignment.Center) {
                CurrentTaskButton(
                    imageRes = R.drawable.ic_check, labelText = stringResource(id = R.string.done),
                    buttonColors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(id = R.color.functionGreen),
                        contentColor = colorResource(id = R.color.primaryWhite))) {}
            }
        }
    }

    @Composable
    private fun CurrentTaskButton(
        @DrawableRes imageRes: Int, labelText: String, buttonColors: ButtonColors,
        onClickListener: () -> Unit) {
        Button(
            onClick = onClickListener,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
            colors = buttonColors) {
            Image(
                painter = painterResource(id = imageRes), modifier = Modifier.size(16.dp),
                contentDescription = labelText, colorFilter = ColorFilter.tint(
                    buttonColors.contentColor(enabled = true).value))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = labelText, fontFamily = TextConfiguration.fontFamily,
                fontWeight = FontWeight.SemiBold, fontSize = 20.sp)
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
}