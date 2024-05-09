package com.igd.snaplingo.core.current_location.domain

import android.location.Location

interface LocationTracker {
    suspend fun getCurrentLocation(): Location?
}