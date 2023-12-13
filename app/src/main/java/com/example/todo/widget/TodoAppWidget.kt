package com.example.todo.widget

import android.content.Context
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.provideContent
import com.example.todo.initKoin
import com.example.todo.ui.screen.MainViewModel
import org.koin.core.component.KoinComponent

class TodoAppWidget : GlanceAppWidget(), KoinComponent {

    override val sizeMode = SizeMode.Responsive(setOf(DpSize(200.dp, 200.dp)))

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val koinApp = initKoin(context)

        provideContent {
            val viewModel = remember { koinApp.koin.get<MainViewModel>() }
            TodoWidget(glanceId = id, viewModel)
        }
    }
}