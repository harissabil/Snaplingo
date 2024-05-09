package com.igd.snaplingo.data.local

import com.igd.snaplingo.data.local.entity.TaskEntity
import com.igd.snaplingo.data.local.room.TaskDao
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val taskDao: TaskDao,
) {

    suspend fun updateTask(taskEntity: TaskEntity) = taskDao.updateTask(taskEntity)

    fun getTaskList() = taskDao.getTaskList()
}