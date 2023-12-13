package com.example.todo.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todo.data.model.Task

@Database(entities = [Task::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}