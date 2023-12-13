package com.example.todo.ui.screen.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todo.data.model.Task
import com.example.todo.ui.components.TasksContainer
import com.example.todo.ui.screen.addTask.AddTaskSheet
import com.example.todo.ui.theme.TodoTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(viewModel: MainViewModel = koinViewModel()) {
    val screenState by viewModel.screenState.collectAsState()
    MainScreenContent(screenState, viewModel)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MainScreenContent(
    state: MainScreenState,
    handler: MainScreenHandler
) {
    val showAddTaskSheet = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(8.dp)
    ) {
        Text(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(Alignment.CenterHorizontally),
            text = "Tasks",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        val pagerState = rememberPagerState(initialPage = Int.MAX_VALUE / 2) { Int.MAX_VALUE }
        HorizontalPager(state = pagerState, verticalAlignment = Alignment.Top) {
            Column {
                TasksContainer(
                    sectionName = "Incomplete",
                    tasks = state.incompleteTasks,
                    onMarkTaskDone = handler::markTaskDone,
                    onDeleteTask = handler::cancelTask,
                    initiallyExpanded = true
                )

                TasksContainer(
                    modifier = Modifier.padding(top = 16.dp),
                    sectionName = "Completed",
                    tasks = state.completedTasks,
                    onMarkTaskDone = handler::markTaskDone,
                    onDeleteTask = handler::deleteTask
                )

                TasksContainer(
                    modifier = Modifier.padding(top = 16.dp),
                    sectionName = "Failed",
                    tasks = state.failedTasks,
                    onMarkTaskDone = {},
                    onDeleteTask = handler::deleteTask
                )
            }
        }

        Spacer(modifier = Modifier.weight(1F))

        FloatingActionButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = { showAddTaskSheet.value = true }
        ) {
            Image(
                painter = painterResource(id = android.R.drawable.ic_input_add),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                contentDescription = null
            )
        }
    }

    AddTaskSheet(show = showAddTaskSheet, onAddTask = handler::addTask)
}

@Preview
@Composable
private fun MainScreenPreview() {
    val tasks = listOf(
        Task(description = "Task 1"),
        Task(description = "Task 2")
    )
    TodoTheme {
        MainScreenContent(
            state = MainScreenState(incompleteTasks = tasks),
            handler = MainScreenHandler
        )
    }
}