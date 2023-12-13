package com.example.todo.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.data.model.Task
import com.example.todo.data.model.TaskState
import com.example.todo.data.repository.TaskRepository
import com.example.todo.util.stateIn
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.util.UUID

class MainViewModel(private val taskRepository: TaskRepository) : ViewModel(), MainScreenHandler {

    val incompleteTasks = taskRepository.getIncompleteTasks().stateIn(this)
    private val completedTasks = taskRepository.getCompletedTasks().stateIn(this)
    private val failedTasks = taskRepository.getFailedTasks().stateIn(this)
    val screenState = combine(incompleteTasks, completedTasks, failedTasks, ::MainScreenState)
        .stateIn(this, MainScreenState())

    val lastTasks = taskRepository.getLastTasks().stateIn(this)

    override fun addTask(description: String) = viewModelScope.launch {
        taskRepository.addTask(description)
    }.let {}

    override fun deleteTask(taskId: UUID) = viewModelScope.launch {
        taskRepository.deleteTask(taskId)
    }.let {}

    override fun cancelTask(taskId: UUID) = viewModelScope.launch {
        taskRepository.setTaskState(taskId, TaskState.Failed)
    }.let {}

    override fun markTaskDone(task: Task) = viewModelScope.launch {
        val state = if (task.state == TaskState.Completed) TaskState.Incomplete else TaskState.Completed
        taskRepository.setTaskState(task.id, state)
    }.let {}
}