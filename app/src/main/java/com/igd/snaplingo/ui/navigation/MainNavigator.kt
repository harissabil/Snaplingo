package com.igd.snaplingo.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.igd.snaplingo.R
import com.igd.snaplingo.ui.component.BottomNavigationItem
import com.igd.snaplingo.ui.component.SnaplingoBottomNavigation
import com.igd.snaplingo.ui.screen.flashcard.FlashcardScreen
import com.igd.snaplingo.ui.screen.history.HistoryScreen
import com.igd.snaplingo.ui.screen.home.HomeScreen
import com.igd.snaplingo.ui.screen.snap.SnapScreen
import com.igd.snaplingo.ui.screen.task.TaskScreen
import kotlinx.coroutines.launch

@Composable
fun MainNavigator() {

    val bottomNavigationItems = remember {
        listOf(
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
        )
    }

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    var selectedItem by rememberSaveable { mutableIntStateOf(0) }
    selectedItem = when (currentRoute) {
        Route.TaskScreen.route -> 0
        Route.FlashcardScreen.route -> 2
        else -> 1
    }

    var isBottomBarVisible by rememberSaveable { mutableStateOf(true) }

    isBottomBarVisible = when (currentRoute) {
        Route.SnapScreen.route -> false
        Route.TaskScreen.route -> false
        Route.FlashcardScreen.route -> false
        Route.HistoryScreen.route -> false
        Route.ProfileScreen.route -> false
        else -> true
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                modifier = if (isBottomBarVisible) Modifier.offset(y = 52.dp) else Modifier,
                hostState = snackbarHostState
            )
        },
        bottomBar = {
            if (isBottomBarVisible) {
                SnaplingoBottomNavigation(
                    items = bottomNavigationItems,
                    selected = selectedItem,
                    onItemClick = { index ->
                        when (index) {
                            0 -> navigateToTab(navController, Route.TaskScreen.route)
                            2 -> navigateToTab(navController, Route.FlashcardScreen.route)
                        }
                    },
                )
            }
        },
        floatingActionButton = {
            if (isBottomBarVisible) {
                Box {
                    FloatingActionButton(
                        onClick = { navController.navigate(Route.SnapScreen.route) },
                        shape = CircleShape,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(72.dp)
                            .offset(y = 52.dp),
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_snap),
                            contentDescription = "Snap",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Route.HomeScreen.route,
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            composable(Route.HomeScreen.route) {
                HomeScreen(
                    innerPadding = innerPadding,
                    snackbarHostState = snackbarHostState,
                    onNavigateToHistory = { navController.navigate(Route.HistoryScreen.route) },
                )
            }
            composable(Route.SnapScreen.route) {
                SnapScreen(
                    snackbarHostState = snackbarHostState,
                    onCameraPermissionDenied = {
                        navController.popBackStack()
                        scope.launch {
                            snackbarHostState.showSnackbar("Camera permission denied")
                        }
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
            composable(Route.TaskScreen.route) {
                TaskScreen(
                    onBackClick = { navController.navigateUp() }
                )
            }
            composable(Route.FlashcardScreen.route) {
                FlashcardScreen(
                    onBackClick = { navController.navigateUp() }
                )
            }
            composable(Route.HistoryScreen.route) {
                HistoryScreen(
                    onBackClick = { navController.navigateUp() })
            }
            composable(Route.ProfileScreen.route) {
                // ProfileScreen()
            }
        }
    }
}

private fun navigateToTab(navController: NavController, route: String) {
    navController.navigate(route) {
        navController.graph.startDestinationRoute?.let { screenRoute ->
            popUpTo(screenRoute) {
                saveState = true
            }
        }
        launchSingleTop = true
        restoreState = true
    }
}
