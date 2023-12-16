package com.example.todo.ui.screen.addTask.schedule

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todo.ui.screen.addTask.OptionSwitcher
import com.example.todo.util.DateFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScheduleSection(
    scheduleState: MutableState<TaskSchedule>,
    modifier: Modifier = Modifier
) {
    var state by scheduleState
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    fun selectOption(option: ScheduleTimeOption) {
        state = state.copy(scheduleOption = option)
    }

    fun setCustomDate(time: Long?) {
        showDatePicker = false
        if (time == null) return selectOption(ScheduleTimeOption.Today)
        state = state.copy(customDate = time)
    }

    Column(modifier = modifier) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(modifier = Modifier.padding(end = 12.dp), text = "Schedule?")
            Switch(
                checked = state.isScheduled,
                onCheckedChange = { state = state.copy(isScheduled = it) }
            )
        }

        val options = listOf(
            ScheduleTimeOption.Today.text, ScheduleTimeOption.Tomorrow.text,
            state.customDate?.let { DateFormatter.millisToString(it) }
                ?: ScheduleTimeOption.CustomDate.text
        )
        AnimatedVisibility(visible = state.isScheduled) {
            OptionSwitcher(
                modifier = Modifier.padding(top = 8.dp),
                options = options,
                selectedIndex = state.scheduleOption.ordinal,
                onSelected = {
                    selectOption(ScheduleTimeOption.entries[it])
                    if (state.scheduleOption == ScheduleTimeOption.CustomDate) showDatePicker = true
                }
            )
        }

        if (showDatePicker) DatePickerDialog(
            onDismissRequest = { setCustomDate(null) },
            confirmButton = {
                TextButton(
                    onClick = { setCustomDate(datePickerState.selectedDateMillis) }
                ) { Text(text = "Confirm") }
            },
            dismissButton = {
                TextButton(
                    onClick = { setCustomDate(null) }
                ) { Text(text = "Cancel") }
            }
        ) { DatePicker(state = datePickerState) }
    }
}

@Preview
@Composable
private fun TaskScheduleSectionPreview() {
    val state = remember { mutableStateOf(TaskSchedule()) }
    TaskScheduleSection(
        modifier = Modifier
            .background(Color.White)
            .padding(8.dp),
        scheduleState = state
    )
}