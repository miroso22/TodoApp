package com.example.todo.widget

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.glance.Button
import androidx.glance.ExperimentalGlanceApi
import androidx.glance.GlanceComposable
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.action.Action
import androidx.glance.action.action
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.CheckBox
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.text.Text
import com.example.todo.data.model.Task
import com.example.todo.ui.screen.MainViewModel
import com.example.todo.widget.popup.PopupActivity
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
@GlanceComposable
fun TodoWidget(glanceId: GlanceId, viewModel: MainViewModel) {
    val tasks by viewModel.lastTasks.collectAsState()

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.tasks.onEach { TodoAppWidget().update(context, glanceId) }.launchIn(this)
    }

    TodoWidgetContent(
        tasks = tasks,
        onAddTask = actionStartActivity(PopupActivity::class.java),
        onMarkTaskDone = viewModel::markTaskDone
    )
}

@OptIn(ExperimentalGlanceApi::class)
@Composable
@GlanceComposable
private fun TodoWidgetContent(
    tasks: List<Task>, onMarkTaskDone: (Task) -> Unit, onAddTask: Action
) = Column(modifier = GlanceModifier.fillMaxSize()) {
    Text(text = "Tasks")
    LazyColumn(activityOptions = bundleOf()) {
        items(tasks) { task -> TaskItem(task = task, onCheck = { onMarkTaskDone(task) }) }
    }
    Button(text = "Add new task", onClick = onAddTask)
}

@Composable
private fun TaskItem(task: Task, onCheck: () -> Unit, modifier: GlanceModifier = GlanceModifier) {
    Row(
        modifier = modifier
            .clickable(block = onCheck)
            .cornerRadius(12.dp)
            .background(color = MaterialTheme.colorScheme.primary)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CheckBox(checked = task.isDone, onCheckedChange = { onCheck() })
        Text(text = task.description)
    }
}

@Preview
@Composable
@GlanceComposable
fun TodoWidgetContentPreview() {
    val tasks = listOf(
        Task(description = "Task 1"),
        Task(description = "Task 2")
    )
    TodoWidgetContent(tasks, onAddTask = action {}, onMarkTaskDone = {})
}