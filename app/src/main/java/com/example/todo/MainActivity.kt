package com.example.todo

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todo.notification.NotificationService
import com.example.todo.ui.navigation.Routes
import com.example.todo.ui.screen.addTask.AddTaskScreen
import com.example.todo.ui.screen.main.MainScreen
import com.example.todo.ui.theme.TodoTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initKoin(this)

        val intent = Intent(this, NotificationService::class.java)
        ContextCompat.startForegroundService(this, intent)

        setContent {
            TodoTheme {
                MainNavigation()
            }
        }
    }

    @Composable
    private fun MainNavigation() {
        val navController = rememberNavController()
        CompositionLocalProvider(LocalNavigator provides navController) {
            NavHost(navController = navController, startDestination = Routes.MAIN_SCREEN) {
                composable(Routes.MAIN_SCREEN) {
                    MainScreen()
                }

                composable(Routes.ADD_TASK) {
                    AddTaskScreen()
                }
            }
        }
    }
}

val LocalNavigator = staticCompositionLocalOf<NavController> { error("No navigator provided") }