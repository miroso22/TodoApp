package com.example.todo

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.content.ContextCompat
import com.example.todo.notification.NotificationService
import com.example.todo.ui.screen.main.MainScreen
import com.example.todo.ui.screen.main.MainViewModel
import com.example.todo.ui.theme.TodoTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initKoin(this)

        val intent = Intent(this, NotificationService::class.java)
        ContextCompat.startForegroundService(this, intent)

        setContent {
            TodoTheme {
                MainScreen(handler = koinViewModel<MainViewModel>())
            }
        }
    }
}