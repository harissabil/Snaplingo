package com.igd.snaplingo.ui.screen.onboarding.presentation.component

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.igd.snaplingo.R
import com.igd.snaplingo.ui.screen.onboarding.domain.model.Page
import com.igd.snaplingo.ui.theme.SnaplingoTheme

@Composable
fun OnBoardingImage(
    modifier: Modifier = Modifier,
    page: Page,
) {
    Box(modifier = modifier) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(fraction = 0.8f),
            painter = painterResource(id = page.image),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }
}

@Preview(showBackground = true)
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun OnBoardingImagePreview() {
    SnaplingoTheme {
        OnBoardingImage(
            page = Page(
                title = "Lorem Ipsum is simply dummy",
                description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.",
                image = R.drawable.onboarding1
            )
        )
    }
}












