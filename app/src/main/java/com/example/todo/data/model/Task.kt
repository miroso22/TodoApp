package com.example.todo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val description: String,
    val isDone: Boolean = false
)