package com.example.todo.ui.screen.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.todo.LocalNavigator
import com.example.todo.R
import com.example.todo.data.model.Task
import com.example.todo.data.model.TaskState
import com.example.todo.ui.components.TasksContainer
import com.example.todo.ui.navigation.Destination
import com.example.todo.ui.navigation.navigateTo
import com.example.todo.ui.theme.TodoTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Locale

private const val DATE_FORMAT = "EEEE, d MMMM, yyyy"

@Composable
fun MainScreen(
    navController: NavController = LocalNavigator.current,
    viewModel: MainViewModel = koinViewModel()
) {
    MainScreenContent(
        handler = viewModel,
        onAddTask = { navController.navigateTo(Destination.AddTask(it)) }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MainScreenContent(handler: MainScreenHandler, onAddTask: (Long) -> Unit) {
    val state by handler.screenState.collectAsState()
    val format = remember { SimpleDateFormat(DATE_FORMAT, Locale.getDefault()) }
    val pagerState = rememberPagerState(initialPage = Int.MAX_VALUE / 2) { Int.MAX_VALUE }
    val scope = rememberCoroutineScope()

    fun scrollToPage(page: Int) = scope.launch { pagerState.animateScrollToPage(page) }

    LaunchedEffect(pagerState.currentPage) {
        handler.setDay(pagerState.currentPage - Int.MAX_VALUE / 2)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { scrollToPage(pagerState.currentPage - 1) }) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_back),
                    tint = MaterialTheme.colorScheme.onBackground,
                    contentDescription = null
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Tasks",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    modifier = Modifier.padding(bottom = 16.dp),
                    text = format.format(state.date.time),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            IconButton(onClick = { scrollToPage(pagerState.currentPage + 1) }) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_forward),
                    tint = MaterialTheme.colorScheme.onBackground,
                    contentDescription = null
                )
            }
        }

        HorizontalPager(
            modifier = Modifier
                .weight(1F)
                .padding(bottom = 24.dp),
            state = pagerState,
            beyondBoundsPageCount = 1,
            verticalAlignment = Alignment.Top,
            pageSpacing = 8.dp
        ) { page ->
            val dayOffset = page - Int.MAX_VALUE / 2
            val tasks = state.tasks[dayOffset].orEmpty()
            Column {
                TasksContainer(
                    sectionName = "Incomplete",
                    tasks = tasks[TaskState.Incomplete].orEmpty(),
                    onMarkTaskDone = handler::markTaskDone,
                    onDeleteTask = handler::cancelTask,
                    initiallyExpanded = true
                )

                TasksContainer(
                    modifier = Modifier.padding(top = 16.dp),
                    sectionName = "Completed",
                    tasks = tasks[TaskState.Completed].orEmpty(),
                    onMarkTaskDone = handler::markTaskDone,
                    onDeleteTask = handler::deleteTask
                )

                TasksContainer(
                    modifier = Modifier.padding(top = 16.dp),
                    sectionName = "Failed",
                    tasks = tasks[TaskState.Failed].orEmpty(),
                    onMarkTaskDone = {},
                    onDeleteTask = handler::deleteTask
                )
            }
        }

        FloatingActionButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = { onAddTask(state.date.timeInMillis) }
        ) {
            Image(
                painter = painterResource(id = android.R.drawable.ic_input_add),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    val tasks = listOf(
        Task(description = "Task 1", state = TaskState.Incomplete),
        Task(description = "Task 2", state = TaskState.Incomplete)
    )
    val state = MainScreenState(tasks = mapOf(0 to tasks.groupBy { it.state }))
    TodoTheme {
        MainScreenContent(
            handler = MainScreenHandler.apply { screenState.value = state },
            onAddTask = {}
        )
    }
}