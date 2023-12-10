package com.example.todo.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.todo.data.model.Task
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks")
    fun getAllTasks(): Flow<List<Task>>

    @Insert
    suspend fun addTask(task: Task)

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteTask(id: UUID)

    @Query("UPDATE tasks SET isDone = :isDone WHERE id = :id")
    suspend fun setDone(id: UUID, isDone: Boolean)

    @Query("SELECT * FROM tasks WHERE isDone = 0 LIMIT :limit")
    suspend fun getLastNTasks(limit: Int): List<Task>

    @Query("SELECT * FROM tasks WHERE isDone = 0 LIMIT :limit")
    fun observeLastNTasks(limit: Int): Flow<List<Task>>
}