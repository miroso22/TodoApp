package com.example.todo.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.data.model.Task
import com.example.todo.data.model.TaskState
import com.example.todo.data.repository.TaskRepository
import com.example.todo.ui.screen.addTask.schedule.TaskSchedule
import com.example.todo.ui.screen.addTask.schedule.scheduledTime
import com.example.todo.util.stateIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.UUID

class MainViewModel(private val taskRepository: TaskRepository) : ViewModel(), MainScreenHandler {

    private val dayOffset = MutableStateFlow(0)
    private val currentDate = dayOffset.map { offset ->
        getCalendarForOffset(offset)
    }.stateIn(this, getCalendarForOffset(0))

    val incompleteTasks = getTasksForDay(TaskState.Incomplete).stateIn(this)
    private val completedTasks = getTasksForDay(TaskState.Completed)
    private val failedTasks = getTasksForDay(TaskState.Failed)

    override val screenState = combine(
        incompleteTasks, completedTasks, failedTasks, currentDate, ::MainScreenState
    ).stateIn(this, MainScreenState())

    val lastTasks = taskRepository.getLastTasks().stateIn(this)

    override fun addTask(description: String, schedule: TaskSchedule) = viewModelScope.launch {
        taskRepository.addTask(description, schedule.scheduledTime)
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

    override fun setDay(dayOffset: Int) {
        this.dayOffset.value = dayOffset
    }

    private fun getCalendarForOffset(offset: Int) = Calendar.getInstance()
        .apply { add(Calendar.DAY_OF_YEAR, offset) }

    private fun getTasksForDay(state: TaskState) =
        currentDate.flatMapLatest { taskRepository.getTasks(it.timeInMillis, state) }
}