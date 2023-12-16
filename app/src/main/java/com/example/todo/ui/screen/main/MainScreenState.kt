package com.example.todo.ui.screen.main

import com.example.todo.data.model.Task
import com.example.todo.ui.screen.addTask.schedule.TaskSchedule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Calendar
import java.util.UUID

data class MainScreenState(
    val incompleteTasks: List<Task> = listOf(),
    val completedTasks: List<Task> = listOf(),
    val failedTasks: List<Task> = listOf(),
    val date: Calendar = Calendar.getInstance()
)

interface MainScreenHandler {
    val screenState: StateFlow<MainScreenState>
    fun addTask(description: String, schedule: TaskSchedule)
    fun markTaskDone(task: Task)
    fun deleteTask(taskId: UUID)
    fun cancelTask(taskId: UUID)
    fun setDay(dayOffset: Int)

    companion object : MainScreenHandler {
        override val screenState = MutableStateFlow(MainScreenState())
        override fun addTask(description: String, schedule: TaskSchedule) = Unit
        override fun markTaskDone(task: Task) = Unit
        override fun deleteTask(taskId: UUID) = Unit
        override fun cancelTask(taskId: UUID) = Unit
        override fun setDay(dayOffset: Int) = Unit
    }
}