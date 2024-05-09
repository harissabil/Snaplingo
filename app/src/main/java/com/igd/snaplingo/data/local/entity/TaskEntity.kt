package com.igd.snaplingo.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "task")
@Parcelize
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val title: String,
    val description: String,
    val isCompleted: Boolean = false,
) : Parcelable
