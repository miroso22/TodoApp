package com.example.todo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "tasks")
data class Task(
    val description: String,
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val state: TaskState = TaskState.Incomplete,
    val timeToDo: Long? = null
)