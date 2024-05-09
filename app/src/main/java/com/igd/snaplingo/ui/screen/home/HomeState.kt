package com.igd.snaplingo.ui.screen.home

import com.google.android.gms.maps.model.LatLng
import com.igd.snaplingo.data.local.entity.SnapEntity

data class HomeState(
    val location: LatLng? = null,
    val snapList: List<SnapEntity> = emptyList(),
)
