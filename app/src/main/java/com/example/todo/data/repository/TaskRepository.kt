package com.example.todo.data.repository

import com.example.todo.data.db.TaskDao
import com.example.todo.data.model.Task
import com.example.todo.data.model.TaskState
import com.example.todo.notification.NotificationService.Companion.NOTIFICATION_TASK_LIMIT
import java.time.Instant
import java.util.Calendar
import java.util.TimeZone
import java.util.UUID
import kotlin.time.Duration.Companion.milliseconds

class TaskRepository(private val taskDao: TaskDao) {

    fun getIncompleteTasks() = taskDao.getTasks(TaskState.Incomplete)
    fun getCompletedTasks() = taskDao.getTasks(TaskState.Completed)
    fun getFailedTasks() = taskDao.getTasks(TaskState.Failed)

    suspend fun addTask(description: String) {
        val date = getStartDayMillis()
        taskDao.addTask(Task(description = description, timeToDo = date))
    }

    suspend fun deleteTask(id: UUID) = taskDao.deleteTask(id)

    suspend fun setDone(id: UUID, isDone: Boolean) =
        taskDao.setState(id, if (isDone) TaskState.Completed else TaskState.Incomplete )

    suspend fun setTaskState(id: UUID, state: TaskState) = taskDao.setState(id, state)

    fun getLastTasks() = taskDao.getLastNTasks(NOTIFICATION_TASK_LIMIT)

    private fun getStartDayMillis(millis: Long = System.currentTimeMillis()): Long {
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        calendar.timeInMillis = millis
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
        return calendar.timeInMillis
    }
}