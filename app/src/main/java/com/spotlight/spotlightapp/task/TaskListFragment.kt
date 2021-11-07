package com.spotlight.spotlightapp.task

import android.graphics.Rect
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.spotlight.spotlightapp.R
import com.spotlight.spotlightapp.databinding.FragmentTaskListBinding
import com.spotlight.spotlightapp.task.adapters.TaskListAdapter

class TaskListFragment : Fragment(R.layout.fragment_task_list) {
    companion object {
        const val TAG = "TaskListFragment"
    }

    private lateinit var viewBinding: FragmentTaskListBinding
    private val viewModel: TaskListViewModel by activityViewModels()
    private val taskListAdapter = TaskListAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding = FragmentTaskListBinding.bind(view)
        configureViews()
        initializeViewModel()
    }

    private fun configureViews() {
        configureTaskRecyclerView()
    }

    private fun configureTaskRecyclerView() {
        viewBinding.taskRecyclerView.apply {
            adapter = taskListAdapter
            setHasFixedSize(true)
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                    super.getItemOffsets(outRect, view, parent, state)

                    outRect.bottom = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        TaskListAdapter.TASK_ITEM_PADDING, resources.displayMetrics).toInt()
                }
            })
        }
    }

    private fun initializeViewModel() {
        viewModel.apply {
            pendingTaskList.observe(viewLifecycleOwner) { tasks ->
                viewBinding.taskRecyclerView.layoutManager?.apply {
                    val savedState = onSaveInstanceState()
                    taskListAdapter.setItems(tasks)
                    onRestoreInstanceState(savedState)
                }
            }

            requestPendingTasks()
        }
    }
}