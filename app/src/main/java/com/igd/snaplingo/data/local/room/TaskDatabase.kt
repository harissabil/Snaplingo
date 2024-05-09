package com.igd.snaplingo.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.igd.snaplingo.data.local.entity.TaskEntity

@Database(
    entities = [TaskEntity::class],
    version = 1,
    exportSchema = true
)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}