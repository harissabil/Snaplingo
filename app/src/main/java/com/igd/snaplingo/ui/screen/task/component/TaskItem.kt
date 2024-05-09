package com.igd.snaplingo.ui.screen.task.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.igd.snaplingo.data.local.entity.TaskEntity
import com.igd.snaplingo.ui.theme.SnaplingoTheme

@Composable
fun TaskItem(
    modifier: Modifier = Modifier,
    task: TaskEntity,
) {
    val alpha = if (task.isCompleted) 0.5f else 1f

    Card(
        modifier = modifier
            .fillMaxWidth()
            .alpha(alpha),
        shape = RectangleShape,
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = task.description,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal,
                )
            }

            Checkbox(checked = task.isCompleted, onCheckedChange = { })
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TaskItemPreview() {
    SnaplingoTheme {
        Surface {
            TaskItem(
                task = TaskEntity(
                    title = "A Day in the Life",
                    description = "Jelajahi aktivitas sehari-hari dan ambil foto 10 objek yang berkaitan dengan kegiatan tersebut.",
                    isCompleted = true
                )
            )
        }
    }
}