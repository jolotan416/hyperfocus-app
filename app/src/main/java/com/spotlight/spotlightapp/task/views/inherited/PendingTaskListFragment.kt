package com.spotlight.spotlightapp.task.views.inherited

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.spotlight.spotlightapp.R
import com.spotlight.spotlightapp.data.task.Task
import com.spotlight.spotlightapp.databinding.FragmentPendingTaskListBinding
import com.spotlight.spotlightapp.task.TaskPageRouter
import com.spotlight.spotlightapp.task.adapters.PendingTaskListAdapter
import com.spotlight.spotlightapp.task.viewmodels.inherited.PendingTaskListViewModel
import com.spotlight.spotlightapp.utilities.viewmodelutils.BaseViewModel
import com.spotlight.spotlightapp.utilities.viewutils.BaseFragment
import com.spotlight.spotlightapp.utilities.viewutils.MarginItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PendingTaskListFragment(private val taskPageRouter: TaskPageRouter)
    : BaseFragment(R.layout.fragment_pending_task_list), PendingTaskListAdapter.Callback {

    private lateinit var viewBinding: FragmentPendingTaskListBinding
    private val pendingTaskListViewModel: PendingTaskListViewModel by viewModels()
    private val pendingTaskListAdapter = PendingTaskListAdapter(this)

    override val viewModel: BaseViewModel
        get() = pendingTaskListViewModel

    override val snackbarLayout: ViewGroup
        get() = viewBinding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding = FragmentPendingTaskListBinding.bind(view)
        configureViews()
        initializeViewModel()
    }

    override fun createTask(view: View) {
        taskPageRouter.openTaskForm(view)
    }

    override fun editTask(view: View, task: Task) {
        taskPageRouter.openTaskPage(view, task, true)
    }

    override fun selectPendingTask(task: Task) {
        pendingTaskListViewModel.selectPendingTask(task)
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
        pendingTaskListViewModel.apply {
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