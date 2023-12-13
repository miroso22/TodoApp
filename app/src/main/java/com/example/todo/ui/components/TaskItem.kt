package com.example.todo.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todo.data.model.Task
import com.example.todo.data.model.TaskState
import com.example.todo.ui.theme.Green
import com.example.todo.ui.theme.GreenSemiTransparent
import com.example.todo.ui.theme.Red
import com.example.todo.ui.theme.RedSemiTransparent
import com.example.todo.ui.theme.Yellow
import com.example.todo.ui.theme.YellowSemiTransparent

@Composable
fun TaskItem(
    task: Task,
    onCheck: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val checkboxColors = CheckboxDefaults.colors(
        checkedColor = if (task.state == TaskState.Failed) Red else Green,
        uncheckedColor = Yellow
    )
    Row(
        modifier = modifier
            .clickable(onClick = onCheck)
            .backgroundFor(task.state)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TriStateCheckbox(
            state = task.state.checkboxState,
            onClick = { onCheck() },
            colors = checkboxColors
        )
        Text(
            modifier = Modifier.weight(1F),
            text = task.description,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        IconButton(onClick = onDelete) {
            Image(
                painter = painterResource(id = android.R.drawable.ic_delete),
                contentDescription = null
            )
        }
    }
}

private fun Modifier.backgroundFor(taskState: TaskState) = this
    .background(
        brush = Brush.horizontalGradient(listOf(taskState.backgroundColor, Color.Transparent)),
        shape = RoundedCornerShape(12.dp)
    )
    .border(width = 2.dp, color = taskState.contentColor, shape = RoundedCornerShape(12.dp))

private val TaskState.contentColor
    get() = when (this) {
        TaskState.Completed -> Green
        TaskState.Incomplete -> Yellow
        TaskState.Failed -> Red
    }

private val TaskState.checkboxState
    get() = when (this) {
        TaskState.Completed -> ToggleableState.On
        TaskState.Incomplete -> ToggleableState.Off
        TaskState.Failed -> ToggleableState.Indeterminate
    }

private val TaskState.backgroundColor
    get() = when (this) {
        TaskState.Completed -> GreenSemiTransparent
        TaskState.Incomplete -> YellowSemiTransparent
        TaskState.Failed -> RedSemiTransparent
    }

@Preview
@Composable
private fun TaskItemPreview() {
    val task = Task(description = "Do something")
    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(8.dp)
    ) {
        TaskItem(task = task.copy(state = TaskState.Completed), onCheck = { }, onDelete = {})
        Spacer(modifier = Modifier.height(8.dp))
        TaskItem(task = task.copy(state = TaskState.Incomplete), onCheck = { }, onDelete = {})
        Spacer(modifier = Modifier.height(8.dp))
        TaskItem(task = task.copy(state = TaskState.Failed), onCheck = { }, onDelete = {})
    }
}