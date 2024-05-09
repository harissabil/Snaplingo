package com.igd.snaplingo.ui.screen.history

import com.igd.snaplingo.data.local.entity.SnapEntity

data class HistoryState(
    val snaps: List<SnapEntity> = emptyList(),
)
