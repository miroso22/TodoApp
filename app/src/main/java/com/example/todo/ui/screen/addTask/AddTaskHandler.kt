package com.example.todo.ui.screen.addTask

import com.example.todo.ui.screen.addTask.schedule.TaskSchedule

interface AddTaskHandler {
    val initialSchedule: TaskSchedule
    fun addTask(description: String, schedule: TaskSchedule)

    companion object : AddTaskHandler {
        override val initialSchedule = TaskSchedule()
        override fun addTask(description: String, schedule: TaskSchedule) = Unit
    }
}