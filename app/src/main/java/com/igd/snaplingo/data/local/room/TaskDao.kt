package com.igd.snaplingo.data.local.room

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.igd.snaplingo.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Update
    suspend fun updateTask(taskEntity: TaskEntity)

    @Query("SELECT * FROM task")
    fun getTaskList(): Flow<List<TaskEntity>>
}