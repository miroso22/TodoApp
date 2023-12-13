package com.example.todo.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todo.data.model.Task
import com.example.todo.ui.theme.Gray
import com.example.todo.ui.theme.GraySemiTransparent
import com.example.todo.ui.theme.TodoTheme
import java.util.UUID

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TasksContainer(
    tasks: List<Task>,
    sectionName: String,
    onMarkTaskDone: (Task) -> Unit,
    onDeleteTask: (UUID) -> Unit,
    modifier: Modifier = Modifier,
    initiallyExpanded: Boolean = false
) {
    var isExpanded by remember { mutableStateOf(initiallyExpanded) }

    Column(
        modifier = modifier
            .clickable { isExpanded = !isExpanded }
            .background(GraySemiTransparent, RoundedCornerShape(12.dp))
            .border(2.dp, Gray, RoundedCornerShape(12.dp))
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1F),
                text = sectionName,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            val icon = if (isExpanded) android.R.drawable.arrow_up_float
            else android.R.drawable.arrow_down_float
            Icon(
                painter = painterResource(id = icon),
                tint = MaterialTheme.colorScheme.onBackground,
                contentDescription = null
            )
        }
        AnimatedVisibility(visible = isExpanded) {
            if (tasks.isEmpty()) return@AnimatedVisibility Text(
                modifier = Modifier.padding(start = 16.dp, top = 8.dp),
                text = "Nothing there yet",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            LazyColumn(
                modifier = Modifier.padding(top = 12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(tasks, key = { it.id }) { task ->
                    TaskItem(
                        modifier = Modifier.animateItemPlacement(),
                        task = task,
                        onCheck = { onMarkTaskDone(task) },
                        onDelete = { onDeleteTask(task.id) }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun TasksContainerPreview() {
    val tasks = listOf(
        Task(description = "Task 1"),
        Task(description = "Task 2")
    )
    TasksContainer(
        modifier = Modifier.background(Color.White).padding(8.dp),
        sectionName = "Incomplete",
        tasks = tasks, initiallyExpanded = true,
        onDeleteTask = {}, onMarkTaskDone = {}
    )
}