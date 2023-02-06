package com.spotlight.spotlightapp.task.views

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.spotlight.spotlightapp.R
import com.spotlight.spotlightapp.data.task.Task
import com.spotlight.spotlightapp.databinding.FragmentPendingTaskListBinding
import com.spotlight.spotlightapp.task.TaskPageRouter
import com.spotlight.spotlightapp.task.adapters.PendingTaskListAdapter
import com.spotlight.spotlightapp.task.viewmodels.PendingTaskListViewModel
import com.spotlight.spotlightapp.utilities.viewmodelutils.ViewModelErrorListener
import com.spotlight.spotlightapp.utilities.viewmodelutils.viewModelErrorListeners
import com.spotlight.spotlightapp.utilities.viewutils.MarginItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PendingTaskListFragment(private val taskPageRouter: TaskPageRouter)
    : Fragment(R.layout.fragment_pending_task_list), PendingTaskListAdapter.Callback {

    private lateinit var viewBinding: FragmentPendingTaskListBinding
    private val viewModel: PendingTaskListViewModel by viewModels()
    private val viewModelErrorListener: ViewModelErrorListener by viewModelErrorListeners()
    private val pendingTaskListAdapter = PendingTaskListAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding = FragmentPendingTaskListBinding.bind(view)
        configureViews()
        initializeViewModel()
        viewModelErrorListener.observeErrors(viewModel, viewBinding.root)
    }

    override fun createTask(view: View) {
        taskPageRouter.openTaskForm(view)
    }

    override fun editTask(view: View, task: Task) {
        taskPageRouter.openTaskPage(view, task, true)
    }

    override fun selectPendingTask(task: Task) {
        viewModel.selectPendingTask(task)
    }

    private fun configureViews() {
        viewBinding.tasksRecyclerView.apply {
            adapter = pendingTaskListAdapter
            setHasFixedSize(true)
            addItemDecoration(
                MarginItemDecoration(resources, PendingTaskListAdapter.TASK_ITEM_PADDING))
        }
    }

    private fun initializeViewModel() {
        viewModel.apply {
            pendingTaskList.observe(viewLifecycleOwner) { tasks ->
                viewBinding.tasksRecyclerView.layoutManager?.apply {
                    val savedState = onSaveInstanceState()
                    pendingTaskListAdapter.setItems(tasks)
                    onRestoreInstanceState(savedState)
                }
            }
        }
    }
}