package com.example.todo.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.todo.data.model.Task
import com.example.todo.data.model.TaskState
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks WHERE state = :state")
    fun getTasks(state: TaskState = TaskState.Incomplete): Flow<List<Task>>

    @Insert
    suspend fun addTask(task: Task)

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteTask(id: UUID)

    @Query("UPDATE tasks SET state = :state WHERE id = :id")
    suspend fun setState(id: UUID, state: TaskState)

    @Query("SELECT * FROM tasks WHERE state = :state LIMIT :limit")
    fun getLastNTasks(limit: Int, state: TaskState = TaskState.Incomplete): Flow<List<Task>>
}