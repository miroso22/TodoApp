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
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.UUID

class MainViewModel(
    private val taskRepository: TaskRepository
) : ViewModel(), MainScreenHandler {

    private val dayOffset = MutableStateFlow(0)

    override val screenState = dayOffset.flatMapLatest { todayOffset ->
        val tomorrowOffset = todayOffset + 1
        val yesterdayOffset = todayOffset - 1
        val taskFlows = listOf(todayOffset, tomorrowOffset, yesterdayOffset).map { offset ->
            val calendar = getCalendarForOffset(offset)
            taskRepository.getTasksForDate(calendar.timeInMillis)
        }
        combine(taskFlows) { (today, tomorrow, yesterday) ->
            val tasks = mapOf(
                todayOffset to today.groupBy { it.state },
                tomorrowOffset to tomorrow.groupBy { it.state },
                yesterdayOffset to yesterday.groupBy { it.state }
            )
            MainScreenState(tasks, getCalendarForOffset(todayOffset))
        }.debounce(200)
    }.stateIn(this, MainScreenState())

    val incompleteTasks = dayOffset.flatMapLatest { offset ->
        val calendar = getCalendarForOffset(offset)
        taskRepository.getTasks(calendar.timeInMillis, TaskState.Incomplete)
    }.stateIn(this)

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
}