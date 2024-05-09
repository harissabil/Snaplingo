package com.igd.snaplingo.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.igd.snaplingo.data.local.entity.SnapEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SnapDao {

    @Upsert
    suspend fun upsertSnap(snapEntity: SnapEntity)

    @Delete
    suspend fun deleteSnap(snapEntity: SnapEntity)

    @Query("SELECT * FROM snap ORDER BY date DESC")
    fun getSnapHistory(): Flow<List<SnapEntity>>

    @Query("SELECT * FROM snap WHERE isSaved = 1 ORDER BY date DESC")
    fun getSavedSnap(): Flow<List<SnapEntity>>

    @Query("SELECT * FROM snap WHERE date = :date")
    suspend fun getSnapByDate(date: String): SnapEntity
}