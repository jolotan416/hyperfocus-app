package com.spotlight.spotlightapp.task

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.spotlight.spotlightapp.R
import com.spotlight.spotlightapp.data.task.Task
import com.spotlight.spotlightapp.databinding.FragmentCompletedTaskListBinding
import com.spotlight.spotlightapp.utilities.BaseViewModel
import com.spotlight.spotlightapp.utilities.ViewModelErrorListener
import com.spotlight.spotlightapp.utilities.observeErrors
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CompletedTaskListFragment : Fragment(R.layout.fragment_completed_task_list),
    ViewModelErrorListener {
    private lateinit var viewBinding: FragmentCompletedTaskListBinding
    private val completedTaskListViewModel: CompletedTaskListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding = FragmentCompletedTaskListBinding.bind(view)
        viewBinding.root.setContent {
            CompletedTaskListLayout()
        }
        observeErrors()
    }

    override val baseViewModel: BaseViewModel
        get() = completedTaskListViewModel

    override val snackbarLayout: View
        get() = viewBinding.root

    @Composable
    fun CompletedTaskListLayout() {
        val completedTaskList = completedTaskListViewModel.completedTaskList.observeAsState()

        if (completedTaskList.value.isNullOrEmpty()) {
            CompletedTaskListEmptyState()
        } else {
            CompletedTaskList(completedTasks = completedTaskList.value!!)
        }
    }

    @Preview
    @Composable
    fun CompletedTaskListEmptyState() {
        Column(
            modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(id = R.string.completed_task_list_empty_state_title),
                color = colorResource(id = R.color.primaryWhite), fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.lato_semibold)), lineHeight = 24.sp,
                textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = R.string.completed_task_list_empty_state_subtitle),
                color = colorResource(id = R.color.primaryWhite), fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.lato_thin)), lineHeight = 20.sp,
                textAlign = TextAlign.Center)
        }
    }

    @Composable
    fun CompletedTaskList(completedTasks: List<Task>) {
        LazyColumn(
            contentPadding = PaddingValues(
                start = 20.dp, top = 20.dp, end = 20.dp, bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(completedTasks) { task ->
                CompletedTaskListCard(task = task)
            }
        }
    }

    @Composable
    fun CompletedTaskListCard(task: Task) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            backgroundColor = colorResource(id = R.color.primaryWhite),
            elevation = 0.dp) {
            Text(
                text = task.title, color = colorResource(id = R.color.primaryBlack),
                fontSize = 20.sp, fontFamily = FontFamily(Font(R.font.lato_semibold)),
                lineHeight = 32.sp, maxLines = 2, overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(12.dp))
        }
    }

    @Preview
    @Composable
    fun CompletedTaskListPreview() {
        CompletedTaskList(
            completedTasks = listOf(
                Task(title = "Task #1", description = "Description of task #1"),
                Task(
                    title = "Long Task #2 so that I could check how it would behave when I put a long title",
                    description = "Description of task #2"),
                Task(
                    title = "Long Task #3 so that I could check how it would behave when I put a long title",
                    description = "Description of task #3"),
                Task(title = "Task #4", description = "Description of task #4")))
    }
}