package com.igd.snaplingo.ui.component

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.igd.snaplingo.R
import com.igd.snaplingo.ui.theme.NoRippleTheme
import com.igd.snaplingo.ui.theme.SnaplingoTheme

@Composable
fun SnaplingoBottomNavigation(
    modifier: Modifier = Modifier,
    items: List<BottomNavigationItem>,
    selected: Int,
    onItemClick: (Int) -> Unit,
) {
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        containerColor = Color.Transparent,
    ) {
        items.forEachIndexed { index, item ->
            if (index == 1) {
                Box(modifier = Modifier.size(32.dp))
                return@forEachIndexed
            }
            CompositionLocalProvider(LocalRippleTheme provides NoRippleTheme) {
                NavigationBarItem(
                    selected = index == selected,
                    onClick = { onItemClick(index) },
                    icon = {
                        (if (index == selected) item.selectedIcon else item.unselectedIcon)?.let {
                            Icon(
                                painter = painterResource(id = it),
                                contentDescription = item.text,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        indicatorColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.0f)
                    ),
                    alwaysShowLabel = false,
                )
            }
        }
    }
}

data class BottomNavigationItem(
    @DrawableRes val selectedIcon: Int?,
    @DrawableRes val unselectedIcon: Int?,
    val text: String?,
)

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SnaplingoBottomNavigationPreview() {
    SnaplingoTheme {
        Surface {
            SnaplingoBottomNavigation(
                items = listOf(
                    BottomNavigationItem(
                        selectedIcon = R.drawable.ic_task_filled,
                        unselectedIcon = R.drawable.ic_task_outlined,
                        text = "Task"
                    ),
                    BottomNavigationItem(
                        selectedIcon = null,
                        unselectedIcon = null,
                        text = null
                    ),
                    BottomNavigationItem(
                        selectedIcon = R.drawable.ic_cognition_filled,
                        unselectedIcon = R.drawable.ic_cognition_outlined,
                        text = "Flashcard"
                    ),
                ), selected = 1,
                onItemClick = {}
            )
        }
    }
}