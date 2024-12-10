package com.example.todo.ui.screen.addTask

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.data.repository.TaskRepository
import com.example.todo.ui.navigation.ParamKey
import com.example.todo.ui.screen.addTask.schedule.ScheduleTimeOption
import com.example.todo.ui.screen.addTask.schedule.TaskSchedule
import com.example.todo.ui.screen.addTask.schedule.scheduledTime
import com.example.todo.util.DateFormatter
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AddTaskViewModel(
    private val taskRepository: TaskRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel(), AddTaskHandler {

    private val initialDate = savedStateHandle.get<Long>(ParamKey.DATE) ?: System.currentTimeMillis()
    override val initialSchedule: TaskSchedule

    init {
        val diff = DateFormatter.startDayMillis(initialDate) - DateFormatter.startDayMillis(System.currentTimeMillis())
        val option = when (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)) {
            0L -> ScheduleTimeOption.Today
            1L -> ScheduleTimeOption.Tomorrow
            else -> ScheduleTimeOption.CustomDate
        }
        val date = if (option == ScheduleTimeOption.CustomDate) initialDate else null
        initialSchedule = TaskSchedule(scheduleOption = option, customDate = date)
    }

    override fun addTask(description: String, schedule: TaskSchedule) = viewModelScope.launch {
        taskRepository.addTask(description, schedule.scheduledTime)
    }.let {}
}