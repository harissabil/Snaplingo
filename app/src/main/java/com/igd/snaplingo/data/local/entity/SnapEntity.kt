package com.igd.snaplingo.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Locale

@Entity(tableName = "snap")
@Parcelize
data class SnapEntity(
    var image: String? = null,
    var label: String,
    var translation: String? = null,
    @PrimaryKey
    var date: String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        .format(java.util.Date()),
    var isSaved: Boolean = false,
    var lat: Double? = null,
    var long: Double? = null,
) : Parcelable