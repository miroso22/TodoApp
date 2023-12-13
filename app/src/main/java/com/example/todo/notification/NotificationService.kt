package com.example.todo.notification

import android.Manifest
import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput
import com.example.todo.data.repository.TaskRepository
import com.example.todo.initKoin
import com.example.todo.util.hasPermission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.util.UUID

class NotificationService : Service() {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    private val taskRepository: TaskRepository by inject()

    override fun onCreate() {
        initKoin(this)
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val handled = handleActions(intent)
        if (handled) return START_STICKY

        observeTasks()
        val notification = createNotification(listOf())
        startForeground(NOTIFICATION_ID, notification)
        return START_STICKY
    }

    @SuppressLint("MissingPermission")
    private fun observeTasks() {
        taskRepository.getLastTasks().onEach { tasks ->
            val notification = createNotification(tasks)
            if (hasPermission(Manifest.permission.POST_NOTIFICATIONS))
                NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, notification)
        }.launchIn(scope)
    }

    private fun handleActions(intent: Intent?): Boolean {
        if (intent == null) return false

        val doneTaskId = intent.extras?.getString(KEY_TASK_ID)
        val newTaskDescription = RemoteInput.getResultsFromIntent(intent)?.getString(KEY_INPUT)

        if (doneTaskId == null && newTaskDescription == null) return false

        scope.launch {
            if (doneTaskId != null) taskRepository.setDone(UUID.fromString(doneTaskId), isDone = true)
            if (newTaskDescription != null) taskRepository.addTask(newTaskDescription)
        }
        return true
    }


    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onBind(intent: Intent?) = null

    companion object {
        const val CHANNEL_ID = "default"
        private const val NOTIFICATION_ID = 1
        const val KEY_INPUT = "input"
        const val KEY_TASK_ID = "taskId"

        const val NOTIFICATION_TASK_LIMIT = 5
    }
}