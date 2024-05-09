package com.igd.snaplingo.ui.screen.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun TopIconButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    contentDescription: String? = null,
    onClick: () -> Unit
) {
    IconButton(
        onClick = { onClick() },
        modifier = Modifier.background(
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.75f),
            shape = CircleShape
        )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription
        )
    }
}