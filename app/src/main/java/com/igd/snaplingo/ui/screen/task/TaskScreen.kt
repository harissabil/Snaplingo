package com.igd.snaplingo.ui.screen.task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.igd.snaplingo.ui.component.NormalTopAppBar
import com.igd.snaplingo.ui.screen.task.component.TaskItem

@Composable
fun TaskScreen(
    viewModel: TaskViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = { NormalTopAppBar(onBackClick = onBackClick, title = "Task") }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Ongoing Tasks",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            val ongoingTasks = state.taskList.filter { !it.isCompleted }

            items(ongoingTasks.size) { index ->
                TaskItem(task = ongoingTasks[index])
            }

            val completedTasks = state.taskList.filter { it.isCompleted }

            if (completedTasks.isNotEmpty()) {
                item {
                    Text(
                        text = "Completed Tasks",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(completedTasks.size) { index ->
                    TaskItem(task = completedTasks[index])
                }
            }
        }
    }
}