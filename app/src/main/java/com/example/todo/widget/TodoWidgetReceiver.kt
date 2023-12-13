package com.example.todo.widget

import androidx.glance.appwidget.GlanceAppWidgetReceiver

class TodoWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget = TodoAppWidget()
}