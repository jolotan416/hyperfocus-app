package com.spotlight.spotlightapp.task.views

import android.graphics.Rect
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.spotlight.spotlightapp.R
import com.spotlight.spotlightapp.data.task.Task
import com.spotlight.spotlightapp.databinding.FragmentPendingTaskListBinding
import com.spotlight.spotlightapp.task.viewmodels.PendingTaskListViewModel
import com.spotlight.spotlightapp.task.adapters.PendingTaskListAdapter
import com.spotlight.spotlightapp.utilities.BaseViewModel
import com.spotlight.spotlightapp.utilities.ViewModelErrorListener
import com.spotlight.spotlightapp.utilities.observeErrors
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PendingTaskListFragment(private val callback: Callback)
    : Fragment(R.layout.fragment_pending_task_list), ViewModelErrorListener,
    PendingTaskListAdapter.PendingTaskListCallback {
    private lateinit var viewBinding: FragmentPendingTaskListBinding
    private val viewModel: PendingTaskListViewModel by viewModels()
    private val pendingTaskListAdapter = PendingTaskListAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding = FragmentPendingTaskListBinding.bind(view)
        configureViews()
        initializeViewModel()
        observeErrors()
    }

    override val baseViewModel: BaseViewModel
        get() = viewModel

    override val snackbarLayout: View
        get() = viewBinding.root

    override fun createTask(view: View) {
        callback.openTaskForm(view)
    }

    override fun editTask(view: View, task: Task) {
        callback.openTaskForm(view, task)
    }

    override fun selectPendingTask(task: Task) {
        viewModel.selectPendingTask(task)
    }

    private fun configureViews() {
        configureTaskRecyclerView()
    }

    private fun configureTaskRecyclerView() {
        viewBinding.tasksRecyclerView.apply {
            adapter = pendingTaskListAdapter
            setHasFixedSize(true)
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                    super.getItemOffsets(outRect, view, parent, state)

                    outRect.bottom = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        PendingTaskListAdapter.TASK_ITEM_PADDING, resources.displayMetrics).toInt()
                }
            })
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

    interface Callback {
        fun openTaskForm(view: View, task: Task? = null)
    }
}