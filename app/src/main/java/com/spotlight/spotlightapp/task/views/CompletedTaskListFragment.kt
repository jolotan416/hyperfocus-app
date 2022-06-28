package com.spotlight.spotlightapp.task.views

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.spotlight.spotlightapp.R
import com.spotlight.spotlightapp.data.task.Task
import com.spotlight.spotlightapp.databinding.FragmentCompletedTaskListBinding
import com.spotlight.spotlightapp.task.TaskPageRouter
import com.spotlight.spotlightapp.task.adapters.CompletedTaskListAdapter
import com.spotlight.spotlightapp.task.viewmodels.CompletedTaskListViewModel
import com.spotlight.spotlightapp.utilities.viewmodelutils.BaseViewModel
import com.spotlight.spotlightapp.utilities.viewmodelutils.ViewModelErrorListener
import com.spotlight.spotlightapp.utilities.viewmodelutils.observeErrors
import com.spotlight.spotlightapp.utilities.viewutils.MarginItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CompletedTaskListFragment(private val taskPageRouter: TaskPageRouter) :
    Fragment(R.layout.fragment_completed_task_list),
    ViewModelErrorListener, CompletedTaskListAdapter.Callback {
    private lateinit var viewBinding: FragmentCompletedTaskListBinding
    private val completedTaskListAdapter = CompletedTaskListAdapter(this)
    private val completedTaskListViewModel: CompletedTaskListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding = FragmentCompletedTaskListBinding.bind(view)
        configureViews()
        observeViewModel()
        observeErrors()
    }

    override val baseViewModel: BaseViewModel
        get() = completedTaskListViewModel

    override val snackbarLayout: View
        get() = viewBinding.root

    override fun onSelectCompletedTask(view: View, task: Task) {
        taskPageRouter.openTaskPage(view, task, false)
    }

    private fun configureViews() {
        viewBinding.tasksRecyclerView.apply {
            adapter = completedTaskListAdapter
            addItemDecoration(
                MarginItemDecoration(resources, CompletedTaskListAdapter.ITEM_PADDING))
            setHasFixedSize(true)
        }
    }

    private fun observeViewModel() {
        completedTaskListViewModel.completedTaskList.observe(viewLifecycleOwner) { completedTasks ->
            viewBinding.willShowEmptyState = completedTasks.isNullOrEmpty()
            completedTaskListAdapter.setItems(completedTasks)
        }
    }
}