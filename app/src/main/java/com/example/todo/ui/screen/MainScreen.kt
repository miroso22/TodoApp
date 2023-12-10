package com.example.todo.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todo.data.model.Task
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(viewModel: MainViewModel = koinViewModel()) {
    val tasks by viewModel.tasks.collectAsState()

    MainScreenContent(
        tasks = tasks,
        onAddTask = viewModel::addTask,
        onMarkTaskDone = viewModel::markTaskDone
    )
}

@Composable
private fun MainScreenContent(
    tasks: List<Task>,
    onAddTask: (String) -> Unit,
    onMarkTaskDone: (Task) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(text = "Tasks", style = MaterialTheme.typography.headlineLarge)
        LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            items(tasks) { task -> TaskItem(task = task, onCheck = { onMarkTaskDone(task) }) }
        }
        var newTaskText by remember { mutableStateOf("") }
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            value = newTaskText,
            onValueChange = { newTaskText = it }
        )

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            enabled = newTaskText.isNotBlank(),
            onClick = { onAddTask(newTaskText) }
        ) {
            Text(text = "Add")
        }
    }
}

@Composable
private fun TaskItem(task: Task, onCheck: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clickable(onClick = onCheck)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(12.dp)
            )
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = task.isDone, onCheckedChange = { onCheck() })
        Text(text = task.description, style = MaterialTheme.typography.titleMedium)
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    val tasks = listOf(
        Task(description = "Task 1"),
        Task(description = "Task 2")
    )
    MainScreenContent(tasks = tasks, onAddTask = {}, onMarkTaskDone = {})
}