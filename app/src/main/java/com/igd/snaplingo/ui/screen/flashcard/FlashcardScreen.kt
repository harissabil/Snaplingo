package com.igd.snaplingo.ui.screen.flashcard

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.igd.snaplingo.ui.component.NormalTopAppBar
import com.igd.snaplingo.ui.screen.flashcard.component.CardItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FlashcardScreen(
    onBackClick: () -> Unit,
    viewModel: FlashcardViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    val pagerState = rememberPagerState(
        initialPage = state.currentSnapIndex,
        initialPageOffsetFraction = 0f,
        pageCount = {
            state.snapList.size
        }
    )

    Scaffold(
        topBar = { NormalTopAppBar(onBackClick = onBackClick, title = "Snap Card") }
    ) { innerPadding ->
        if (state.snapList.isNotEmpty()) {
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
                    .padding(top = 8.dp)
                    .padding(horizontal = 32.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "${pagerState.currentPage + 1}/${state.snapList.size}",
                    style = MaterialTheme.typography.bodyMedium
                )
                LinearProgressIndicator(
                    progress = {
                        if (state.snapList.isNotEmpty()) {
                            (pagerState.currentPage + 1).toFloat() / state.snapList.size.toFloat()
                        } else {
                            0f
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
            HorizontalPager(
                modifier = Modifier.fillMaxSize(),
                state = pagerState,
                contentPadding = PaddingValues(horizontal = 32.dp),
                pageSpacing = 16.dp
            ) { index ->
                CardItem(
                    modifier = Modifier.padding(innerPadding),
                    snapEntity = state.snapList[index]
                )
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "No data", style = MaterialTheme.typography.headlineLarge)
            }
        }
    }
}