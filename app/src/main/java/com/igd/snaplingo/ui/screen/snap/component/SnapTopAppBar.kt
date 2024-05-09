package com.igd.snaplingo.ui.screen.snap.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SnapTopAppBar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
) {
    TopAppBar(
        colors = TopAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White,
            actionIconContentColor = Color.White,
            scrolledContainerColor = Color.Transparent
        ),
        title = {
            Text(text = "Snap Translator")
        },
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                )
            }
        }
    )
}