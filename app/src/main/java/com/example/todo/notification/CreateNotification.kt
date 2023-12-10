package com.example.todo.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput
import androidx.core.os.bundleOf
import com.example.todo.R
import com.example.todo.data.model.Task
import com.example.todo.notification.NotificationService.Companion.NOTIFICATION_TASK_LIMIT
import kotlin.random.Random

fun NotificationService.createNotification(tasksToDisplay: List<Task>): Notification {
    val inputAction = createInputAction(this)

    val layout = RemoteViews(packageName, R.layout.layout_custom_notification)
    for (index in 0 until NOTIFICATION_TASK_LIMIT) {
        layout.initTaskView(this, tasksToDisplay, index)
    }

    return NotificationCompat.Builder(this, NotificationService.CHANNEL_ID)
        .setSmallIcon(android.R.drawable.ic_delete)
        .addAction(inputAction)
        .setAutoCancel(true)
        .setSilent(true)
        .setOngoing(true)
        .setStyle(NotificationCompat.DecoratedCustomViewStyle())
        .setCustomContentView(layout)
        .setPriority(NotificationCompat.PRIORITY_MAX)
        .build()
}

fun NotificationService.createNotificationChannel() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
    val channel = NotificationChannel(NotificationService.CHANNEL_ID, "TODO app", NotificationManager.IMPORTANCE_DEFAULT)
    NotificationManagerCompat.from(this).createNotificationChannel(channel)
}

private fun createInputAction(context: Context): NotificationCompat.Action {
    val pendingIntent = createPendingIntent(context)

    val label = "What do you need to do?"
    val remoteInput = RemoteInput.Builder(NotificationService.KEY_INPUT)
        .setLabel(label)
        .build()

    val icon = android.R.drawable.ic_delete
    val title = "Add a task"
    return NotificationCompat.Action.Builder(icon, title, pendingIntent)
        .addRemoteInput(remoteInput)
        .build()
}

private fun RemoteViews.initTaskView(context: Context, tasks: List<Task>, index: Int) {
    val isVisible = index < tasks.size
    val visibility = if (isVisible) View.VISIBLE else View.GONE
    setViewVisibility(taskContainerIds[index], visibility)
    if (!isVisible) return

    val task = tasks[index]
    setTextViewText(taskDescriptionIds[index], task.description)

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) return

    val extras = bundleOf(NotificationService.KEY_TASK_ID to task.id.toString())
    val pendingIntent = createPendingIntent(context, extras)
    val response = RemoteViews.RemoteResponse.fromPendingIntent(pendingIntent)
    setOnCheckedChangeResponse(taskCheckboxIds[index], response)
}

private fun createPendingIntent(context: Context, extras: Bundle = bundleOf()): PendingIntent {
    val intent = Intent(context, NotificationService::class.java)
    intent.putExtras(extras)
    val flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
    val requestCode = Random.nextInt()
    return PendingIntent.getService(context.applicationContext, requestCode, intent, flags)
}

private val taskContainerIds = listOf(R.id.task1, R.id.task2, R.id.task3, R.id.task4, R.id.task5)
private val taskDescriptionIds = listOf(R.id.description1, R.id.description2, R.id.description3, R.id.description4, R.id.description5)
private val taskCheckboxIds = listOf(R.id.checkBox1, R.id.checkBox2, R.id.checkBox3, R.id.checkBox4, R.id.checkBox5)