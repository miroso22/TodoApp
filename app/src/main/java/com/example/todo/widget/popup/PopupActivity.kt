package com.example.todo.widget.popup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.todo.initKoin
import com.example.todo.ui.theme.TodoTheme

class PopupActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initKoin(this)

        setContent {
            TodoTheme {
                WidgetPopup()
            }
        }
    }
}