package com.example.todo.ui.screen.main

import com.example.todo.data.model.Task
import java.util.UUID

data class MainScreenState(
    val incompleteTasks: List<Task> = listOf(),
    val completedTasks: List<Task> = listOf(),
    val failedTasks: List<Task> = listOf(),
)

interface MainScreenHandler {
    fun addTask(description: String)
    fun markTaskDone(task: Task)
    fun deleteTask(taskId: UUID)
    fun cancelTask(taskId: UUID)

    companion object : MainScreenHandler {
        override fun addTask(description: String) = Unit
        override fun markTaskDone(task: Task) = Unit
        override fun deleteTask(taskId: UUID) = Unit
        override fun cancelTask(taskId: UUID) = Unit
    }
}