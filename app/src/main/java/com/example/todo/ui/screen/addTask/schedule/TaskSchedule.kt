package com.example.todo.ui.screen.addTask.schedule

import androidx.compose.runtime.Stable
import java.util.Calendar

@Stable
data class TaskSchedule(
    val isScheduled: Boolean = true,
    val scheduleOption: ScheduleTimeOption = ScheduleTimeOption.Today,
    val customDate: Long? = null
)

@Stable
enum class ScheduleTimeOption(val text: String) {
    Today("Today"), Tomorrow("Tomorrow"), CustomDate("Select date")
}

val TaskSchedule.scheduledTime: Long? get() {
    if (!isScheduled) return null
    val calendar = Calendar.getInstance()
    when (scheduleOption) {
        ScheduleTimeOption.Today -> {}
        ScheduleTimeOption.Tomorrow -> { calendar.add(Calendar.DAY_OF_YEAR, 1) }
        ScheduleTimeOption.CustomDate -> { customDate?.let { calendar.timeInMillis = it }  }
    }
    return calendar.timeInMillis
}
