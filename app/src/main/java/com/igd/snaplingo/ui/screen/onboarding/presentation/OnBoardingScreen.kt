package com.igd.snaplingo.ui.screen.onboarding.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.igd.snaplingo.ui.screen.onboarding.domain.model.pages
import com.igd.snaplingo.ui.screen.onboarding.presentation.component.OnBoardingCard
import com.igd.snaplingo.ui.screen.onboarding.presentation.component.OnBoardingImage
import com.igd.snaplingo.ui.screen.onboarding.presentation.component.PagerIndicator
import com.igd.snaplingo.ui.theme.SnaplingoTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBoardingScreen(
    viewModel: OnBoardingViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val pagerState = rememberPagerState(initialPage = 0) {
        pages.size
    }

    val buttonText by remember {
        derivedStateOf {
            when (pagerState.currentPage) {
                0 -> listOf("", "Next")
                pages.size - 1 -> listOf("Back", "Get Started")
                else -> listOf("Back", "Next")
            }
        }
    }

    fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    val buttonStates = remember {
        List(pages.size) {
            mutableStateOf(true)
        }
    }

    val requestLocationPermissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val fineLocationPermission =
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
            val coarseLocationPermission =
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
            buttonStates[pages.size - 1].value = fineLocationPermission && coarseLocationPermission
        }

    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.currentPage == pages.size - 1) {
            val fineLocationPermission = checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            val coarseLocationPermission =
                checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
            buttonStates[pages.size - 1].value = fineLocationPermission && coarseLocationPermission
        }
    }


    OnBoardingContent(
        pagerState = pagerState,
        buttonText = buttonText,
        buttonState = buttonStates[pagerState.currentPage].value,
        onGetStartedClick = viewModel::saveAppEntry,
        onRequestLocationPermission = {
            requestLocationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBoardingContent(
    pagerState: PagerState,
    buttonText: List<String>,
    buttonState: Boolean,
    onGetStartedClick: () -> Unit = {},
    onRequestLocationPermission: () -> Unit = {},
) {
    Box(modifier = Modifier.fillMaxSize()) {

        HorizontalPager(state = pagerState) { index ->
            OnBoardingImage(page = pages[index])
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OnBoardingCard(pagerState = pagerState)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .animateContentSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                if (pagerState.currentPage == pages.size - 1) {
                    Spacer(modifier = Modifier.height(32.dp))
                    FilledTonalButton(
                        onClick = onRequestLocationPermission
                    ) {
                        Text(text = "Set location permission")
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .navigationBarsPadding(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                PagerIndicator(
                    modifier = Modifier.width(44.dp),
                    pagesSize = pages.size,
                    selectedPage = pagerState.currentPage
                )
                Spacer(modifier = Modifier.weight(1f))
                Row(verticalAlignment = Alignment.CenterVertically) {

                    val scope = rememberCoroutineScope()

                    AnimatedVisibility(
                        visible = buttonText[0].isNotEmpty(),
                        enter = fadeIn(), exit = fadeOut()
                    ) {
                        TextButton(
                            onClick = {
                                scope.launch {
                                    pagerState.animateScrollToPage(page = pagerState.currentPage - 1)
                                }
                            }
                        ) {
                            Text(text = buttonText[0])
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            scope.launch {
                                if (pagerState.currentPage == pages.size - 1) {
                                    onGetStartedClick()
                                } else {
                                    pagerState.animateScrollToPage(page = pagerState.currentPage + 1)
                                }
                            }
                        },
                        enabled = buttonState,
                    ) {
                        Text(text = buttonText[1])
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun OnBoardingContentPreview() {
    SnaplingoTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            OnBoardingContent(
                pagerState = rememberPagerState(pageCount = { 3 }),
                buttonText = listOf("Back", "Next"),
                buttonState = true,
                onGetStartedClick = {}
            )
        }
    }
}