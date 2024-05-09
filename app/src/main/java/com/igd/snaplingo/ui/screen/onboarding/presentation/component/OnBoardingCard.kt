package com.igd.snaplingo.ui.screen.onboarding.presentation.component

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.igd.snaplingo.ui.screen.onboarding.domain.model.pages
import com.igd.snaplingo.ui.theme.SnaplingoTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBoardingCard(
    pagerState: PagerState,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Column {
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                modifier = Modifier
                    .animateContentSize()
                    .padding(horizontal = 32.dp),
                text = pages[pagerState.currentPage].title,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                modifier = Modifier
                    .animateContentSize()
                    .padding(horizontal = 32.dp),
                text = pages[pagerState.currentPage].description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun OnBoardingCardPreview() {
    SnaplingoTheme {
        OnBoardingCard(
            pagerState = rememberPagerState {
                pages.size
            }
        )
    }
}