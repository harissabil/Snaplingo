package com.igd.snaplingo.ui.screen.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.igd.snaplingo.data.local.SnapRepository
import com.igd.snaplingo.data.local.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val snapRepository: SnapRepository,
    private val taskRepository: TaskRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(TaskState())
    val state: StateFlow<TaskState> = _state.asStateFlow()

    init {
        getTaskList()
        getSnapList()
        updateTask()
    }

    private fun getTaskList() = viewModelScope.launch {
        taskRepository.getTaskList().collectLatest {
            _state.value = _state.value.copy(taskList = it)
        }
    }

    private fun getSnapList() = viewModelScope.launch {
        snapRepository.getSavedSnap().collectLatest {
            _state.value = _state.value.copy(snapList = it)
        }
    }

    private fun updateTask() = viewModelScope.launch {
        snapRepository.getSavedSnap().collectLatest { snapList ->
            taskRepository.getTaskList().collectLatest {
                val tasksToUpdate = listOf(
                    Pair(10, it.getOrNull(0)),
                    Pair(20, it.getOrNull(1)),
                    Pair(40, it.getOrNull(3)),
                    Pair(50, it.getOrNull(4))
                )

                for ((threshold, task) in tasksToUpdate) {
                    if (snapList.size >= threshold && task != null && !task.isCompleted) {
                        taskRepository.updateTask(task.copy(isCompleted = true))
                    }
                }
            }
        }
    }
}