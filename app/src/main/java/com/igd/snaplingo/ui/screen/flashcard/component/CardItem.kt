package com.igd.snaplingo.ui.screen.flashcard.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.igd.snaplingo.data.local.entity.SnapEntity

@Composable
fun CardItem(
    modifier: Modifier = Modifier,
    snapEntity: SnapEntity,
) {

    var rotated by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue = if (rotated) 180f else 0f,
        animationSpec = tween(500),
        label = "rotation"
    )

    val animateFront by animateFloatAsState(
        targetValue = if (!rotated) 1f else 0f,
        animationSpec = tween(500),
        label = "animateFront"
    )

    val animateBack by animateFloatAsState(
        targetValue = if (rotated) 1f else 0f,
        animationSpec = tween(500),
        label = "animateBack"
    )

    Card(
        modifier = modifier
            .height(220.dp)
            .fillMaxWidth()
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 8 * density
            }
            .clickable {
                rotated = !rotated
            },
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
        ),
    ) {
        if (!rotated) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .graphicsLayer {
                        alpha = animateFront
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = snapEntity.label,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .graphicsLayer {
                        alpha = animateBack
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = snapEntity.translation ?: "N/A",
                    color = Color.Black,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer {
                            alpha = animateBack
                            rotationY = rotation
                        }
                )
            }
        }
    }
}