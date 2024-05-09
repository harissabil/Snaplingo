package com.igd.snaplingo.ui.screen.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.igd.snaplingo.data.local.entity.SnapEntity
import com.igd.snaplingo.ui.component.NormalTopAppBar
import com.igd.snaplingo.ui.screen.history.component.HistoryItem
import timber.log.Timber

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
) {
    val state by viewModel.state.collectAsState()
    val lazyListState = rememberLazyListState()

    Scaffold(
        topBar = { NormalTopAppBar(onBackClick = onBackClick, title = "History") }
    ) { innerPadding ->
        Timber.d("HistoryScreen: innerPadding: $innerPadding")
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            state = lazyListState,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(
                items = state.snaps,
                key = { _: Int, item: SnapEntity -> item.date }
            ) { _, item ->
                HistoryItem(snapEntity = item)
            }
        }
    }
}