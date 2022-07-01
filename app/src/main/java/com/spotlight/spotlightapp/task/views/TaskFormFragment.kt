package com.spotlight.spotlightapp.task.views

import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.spotlight.spotlightapp.R
import com.spotlight.spotlightapp.databinding.FragmentTaskFormBinding
import com.spotlight.spotlightapp.task.viewdata.TaskTransitionName
import com.spotlight.spotlightapp.task.viewmodels.TaskFormViewModel
import com.spotlight.spotlightapp.utilities.viewmodelutils.ErrorHolder
import com.spotlight.spotlightapp.utilities.viewmodelutils.ViewModelErrorListener
import com.spotlight.spotlightapp.utilities.viewmodelutils.observeErrors
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TaskFormFragment : Fragment(R.layout.fragment_task_form), ViewModelErrorListener {
    companion object {
        const val TAG = "TaskFormFragment"

        const val TASK = "task"
    }

    @Inject
    lateinit var mErrorHolder: ErrorHolder

    private val taskFormViewModel: TaskFormViewModel by viewModels()
    private lateinit var binding: FragmentTaskFormBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = savedInstanceState ?: arguments
        taskFormViewModel.setTask(bundle?.getParcelable(TASK))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentTaskFormBinding.bind(view).apply {
            lifecycleOwner = viewLifecycleOwner
            taskFormViewModel = this@TaskFormFragment.taskFormViewModel
        }
        configureViews()
        observeViewModel()
        observeErrors()
        startPostponedEnterTransition()
    }

    override val errorHolder: ErrorHolder
        get() = mErrorHolder

    override val snackbarLayout: View
        get() = binding.root

    private fun configureViews() {
        val viewModel = taskFormViewModel

        binding.apply {
            root.transitionName = TaskTransitionName.TASK_FORM.getTransitionName(
                viewModel.initialTask.value?.id?.toString() ?: "")

            taskTitleEditText.apply {
                imeOptions = EditorInfo.IME_ACTION_NEXT
                setRawInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES)

                setOnFocusChangeListener { _, hasFocus ->
                    viewModel.updateTitleCharacterCountVisibility(hasFocus)
                }

                doAfterTextChanged {
                    viewModel.updateTitle(it?.toString())
                }
            }

            taskDescriptionEditText.doAfterTextChanged {
                viewModel.updateDescription(it?.toString())
            }

            toolbar.setActionButtonTextClickListener {
                viewModel.saveTask()
            }
        }
    }

    private fun observeViewModel() {
        taskFormViewModel.isFormSubmitted.observe(viewLifecycleOwner) {
            parentFragmentManager.popBackStack()
        }
    }
}