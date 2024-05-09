package com.igd.snaplingo.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.igd.snaplingo.data.local.entity.SnapEntity

@Database(
    entities = [SnapEntity::class],
    version = 1,
    exportSchema = true
)
abstract class SnapDatabase : RoomDatabase() {
    abstract fun snapDao(): SnapDao
}