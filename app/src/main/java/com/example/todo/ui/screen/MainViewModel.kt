package com.example.todo.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.data.model.Task
import com.example.todo.data.repository.TaskRepository
import com.example.todo.util.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class MainViewModel(private val taskRepository: TaskRepository) : ViewModel() {

    val tasks = taskRepository.getTasks().stateIn(this)
    val lastTasks = taskRepository.observeLastTasks().stateIn(this)

    fun addTask(description: String) = viewModelScope.launch {
        taskRepository.addTask(description)
    }

    fun deleteTask(taskId: UUID) = viewModelScope.launch {
        taskRepository.deleteTask(taskId)
    }

    fun markTaskDone(task: Task) = viewModelScope.launch {
        taskRepository.setDone(task.id, !task.isDone)
    }
}