package com.igd.snaplingo.ui.screen.flashcard

import com.igd.snaplingo.data.local.entity.SnapEntity

data class FlashcardState(
    val snapList: List<SnapEntity> = emptyList(),
    val currentSnapIndex: Int = 0,
)
