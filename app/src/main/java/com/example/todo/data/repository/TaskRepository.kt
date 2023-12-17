package com.example.todo.data.repository

import com.example.todo.data.db.TaskDao
import com.example.todo.data.model.Task
import com.example.todo.data.model.TaskState
import com.example.todo.notification.NotificationService.Companion.NOTIFICATION_TASK_LIMIT
import com.example.todo.util.DateFormatter
import java.util.UUID

class TaskRepository(private val taskDao: TaskDao) {

    fun getTasks(date: Long, state: TaskState = TaskState.Incomplete) =
        taskDao.getTasks(DateFormatter.millisToString(date), state)

    fun getTasksForDate(date: Long) =
        taskDao.getTasksForDay(DateFormatter.millisToString(date))

    suspend fun addTask(description: String, scheduledTime: Long?) {
        val date = scheduledTime?.let { DateFormatter.millisToString(it) }
        taskDao.addTask(Task(description = description, timeToDo = date))
    }

    suspend fun deleteTask(id: UUID) = taskDao.deleteTask(id)

    suspend fun setDone(id: UUID, isDone: Boolean) =
        taskDao.setState(id, if (isDone) TaskState.Completed else TaskState.Incomplete )

    suspend fun setTaskState(id: UUID, state: TaskState) = taskDao.setState(id, state)

    fun getLastTasks() = taskDao.getLastNTasks(NOTIFICATION_TASK_LIMIT)
}