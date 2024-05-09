package com.igd.snaplingo.ui.screen.onboarding.domain.model

import androidx.annotation.DrawableRes
import com.igd.snaplingo.R

data class Page(
    val title: String,
    val description: String,
    @DrawableRes val image: Int,
)

val pages = listOf(
    Page(
        title = "Snap and Learn",
        description = "Snap a picture of your surroundings and learn new words",
        image = R.drawable.onboarding1
    ),
    Page(
        title = "Task and Flashcard",
        description = "Complete tasks and use flashcards to memorize new words",
        image = R.drawable.onboarding2
    ),
    Page(
        title = "Explore and Discover",
        description = "Explore the world around you and discover new words",
        image = R.drawable.onboarding3
    )
)