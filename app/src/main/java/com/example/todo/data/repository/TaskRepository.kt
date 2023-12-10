package com.example.todo.data.repository

import com.example.todo.data.db.TaskDao
import com.example.todo.data.model.Task
import com.example.todo.notification.NotificationService.Companion.NOTIFICATION_TASK_LIMIT
import java.util.UUID

class TaskRepository(private val taskDao: TaskDao) {

    fun getTasks() = taskDao.getAllTasks()

    suspend fun addTask(description: String) {
        val task = Task(UUID.randomUUID(), description, isDone = false)
        taskDao.addTask(task)
    }

    suspend fun deleteTask(id: UUID) = taskDao.deleteTask(id)

    suspend fun setDone(id: UUID, isDone: Boolean) = taskDao.setDone(id, isDone)

    suspend fun getLastTasks() = taskDao.getLastNTasks(NOTIFICATION_TASK_LIMIT)

    fun observeLastTasks() = taskDao.observeLastNTasks(NOTIFICATION_TASK_LIMIT)
}