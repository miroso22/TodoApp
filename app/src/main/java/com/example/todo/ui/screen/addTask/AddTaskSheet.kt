package com.example.todo.ui.screen.addTask

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todo.ui.screen.addTask.schedule.TaskScheduleSection
import com.example.todo.ui.screen.addTask.schedule.TaskSchedule

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskSheet(
    show: MutableState<Boolean>,
    onAddTask: (String, TaskSchedule) -> Unit
) {
    if (!show.value) return
    ModalBottomSheet(onDismissRequest = { show.value = false }) {
        AddTaskSheetContent(
            onAddTask = { text, schedule -> onAddTask(text, schedule); show.value = false }
        )
    }
}

@Composable
private fun AddTaskSheetContent(
    onAddTask: (String, TaskSchedule) -> Unit,
    modifier: Modifier = Modifier
) = Column(
    modifier = modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp)
) {
    var newTaskText by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val taskSchedule = remember { mutableStateOf(TaskSchedule()) }

    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .focusRequester(focusRequester),
        value = newTaskText,
        label = { Text(text = "What do you need to do?") },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { onAddTask(newTaskText, taskSchedule.value) }),
        onValueChange = { newTaskText = it }
    )


    TaskScheduleSection(
        modifier = Modifier.padding(top = 12.dp),
        scheduleState = taskSchedule
    )

    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, bottom = 36.dp),
        enabled = newTaskText.isNotBlank(),
        onClick = { onAddTask(newTaskText, taskSchedule.value) }
    ) {
        Text(text = "Add")
    }
}

@Preview
@Composable
private fun AddTaskSheetPreview() {
    AddTaskSheetContent(modifier = Modifier.background(Color.White), onAddTask = { _, _ -> })
}