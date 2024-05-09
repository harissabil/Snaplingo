package com.igd.snaplingo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.igd.snaplingo.ui.screen.onboarding.presentation.OnBoardingScreen

@Composable
fun NavGraph(
    startDestination: String,
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {
        navigation(
            route = Route.AppStartNavigation.route,
            startDestination = Route.OnBoardingScreen.route
        ) {
            composable(route = Route.OnBoardingScreen.route) {
                OnBoardingScreen()
            }
        }

        navigation(
            route = Route.SignInNavigation.route,
            startDestination = Route.SignInScreen.route
        ) {
            composable(route = Route.SignInScreen.route) {
                //
            }
        }

        navigation(
            route = Route.MainNavigation.route,
            startDestination = Route.MainNavigator.route
        ) {
            composable(route = Route.MainNavigator.route) {
                MainNavigator()
            }
        }
    }
}