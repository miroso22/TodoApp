package com.example.todo.ui.screen.addTask

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todo.LocalNavigator
import com.example.todo.R
import com.example.todo.ui.screen.addTask.schedule.TaskScheduleSection
import org.koin.androidx.compose.getViewModel

@Composable
fun AddTaskScreen(viewModel: AddTaskViewModel = getViewModel()) {
    val navController = LocalNavigator.current
    AddTaskSheetContent(
        handler = viewModel,
        onBack = { navController.popBackStack() }
    )
}

@Composable
private fun AddTaskSheetContent(
    handler: AddTaskHandler,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) = Column(
    modifier = modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .padding(8.dp)
) {
    var newTaskText by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val taskSchedule = remember { mutableStateOf(handler.initialSchedule) }
    val addButtonEnabled by remember { derivedStateOf { newTaskText.isNotBlank() } }

    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    IconButton(onClick = onBack) {
        Icon(
            painter = painterResource(id = R.drawable.arrow_back),
            tint = MaterialTheme.colorScheme.onBackground,
            contentDescription = null
        )
    }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .focusRequester(focusRequester),
        value = newTaskText,
        label = { Text(text = "What do you need to do?") },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {
            handler.addTask(newTaskText, taskSchedule.value)
            onBack()
        }),
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
        enabled = addButtonEnabled,
        onClick = { handler.addTask(newTaskText, taskSchedule.value); onBack() }
    ) {
        Text(text = "Add")
    }
}

@Preview
@Composable
private fun AddTaskSheetPreview() {
    AddTaskSheetContent(
        modifier = Modifier.background(Color.White),
        handler = AddTaskHandler, onBack = {}
    )
}