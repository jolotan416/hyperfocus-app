package com.spotlight.spotlightapp.task.viewmodels

import androidx.lifecycle.*
import com.spotlight.spotlightapp.data.task.Task
import com.spotlight.spotlightapp.task.repo.TasksRepository
import com.spotlight.spotlightapp.task.viewdata.CharacterCountData
import com.spotlight.spotlightapp.utilities.viewmodelutils.ErrorHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskFormViewModel @Inject constructor(
    private val errorHolder: ErrorHolder, private val tasksRepository: TasksRepository) :
    ViewModel() {
    companion object {
        private const val MAX_TASK_TITLE_CHARACTERS = 140
    }

    private val mutableInitialTask: MutableLiveData<Task> by lazy {
        MutableLiveData<Task>()
    }

    private val mutableTitleCharacterCountData: MutableLiveData<CharacterCountData> by lazy {
        MutableLiveData<CharacterCountData>(
            CharacterCountData(
                MAX_TASK_TITLE_CHARACTERS,
                MAX_TASK_TITLE_CHARACTERS, false))
    }

    private val mutableHasValidTitle: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }

    private val mutableHasValidDescription: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }

    private val mutableIsFormSubmitted: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    private var title: String = ""
        set(value) {
            field = value

            mutableHasValidTitle.value = value.isNotBlank() && (value != initialTask.value?.title)
        }

    private var description: String = ""
        set(value) {
            field = value

            mutableHasValidDescription.value = value.isNotBlank() && (value != initialTask.value?.description)
        }

    val initialTask: LiveData<Task> = mutableInitialTask
    val titleCharacterCountData: LiveData<CharacterCountData> = mutableTitleCharacterCountData
    val isFormValid: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        val onChangedListener = {
            value = mutableHasValidTitle.value!! && mutableHasValidDescription.value!!
        }
        addSource(mutableHasValidTitle) { onChangedListener() }
        addSource(mutableHasValidDescription) { onChangedListener() }
    }
    val isFormSubmitted: LiveData<Boolean> = mutableIsFormSubmitted

    fun setTask(task: Task?) {
        task?.let {
            mutableInitialTask.value = it
            updateTitle(it.title)
            updateDescription(it.description)
        }
    }

    fun updateTitle(title: String?) {
        title.let {
            this.title = it ?: ""
            val charactersRemaining = MAX_TASK_TITLE_CHARACTERS - this.title.length
            mutableTitleCharacterCountData.value = titleCharacterCountData.value!!.apply {
                this.charactersRemaining = charactersRemaining
            }
            updateTitleCharacterCountVisibility(true)
        }
    }

    fun updateTitleCharacterCountVisibility(hasFocus: Boolean) {
        mutableTitleCharacterCountData.value = titleCharacterCountData.value!!.apply {
            willShowCharactersRemaining = hasFocus || (charactersRemaining != MAX_TASK_TITLE_CHARACTERS)
        }
    }

    fun updateDescription(description: String?) {
        this.description = description ?: ""
    }

    fun saveTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = if (initialTask.value == null) {
                tasksRepository.insertTask(Task(title = title, description = description))
            } else {
                tasksRepository.updateTask(initialTask.value!!.also {
                    it.title = title
                    it.description = description
                })
            }

            errorHolder.handleRepositoryResult(result) {
                mutableIsFormSubmitted.value = true
            }
        }
    }
}