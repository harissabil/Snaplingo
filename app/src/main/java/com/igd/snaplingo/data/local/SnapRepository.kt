package com.igd.snaplingo.data.local

import com.igd.snaplingo.data.local.entity.SnapEntity
import com.igd.snaplingo.data.local.room.SnapDao
import javax.inject.Inject

class SnapRepository @Inject constructor(
    private val snapDao: SnapDao
) {

    suspend fun upsertSnap(snapEntity: SnapEntity) = snapDao.upsertSnap(snapEntity)

    suspend fun deleteSnap(snapEntity: SnapEntity) = snapDao.deleteSnap(snapEntity)

    fun getSnapHistory() = snapDao.getSnapHistory()

    fun getSavedSnap() = snapDao.getSavedSnap()

    suspend fun getSnapByDate(date: String) = snapDao.getSnapByDate(date)
}