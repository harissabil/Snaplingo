package com.igd.snaplingo.ui.navigation

sealed class Route(val route: String) {
    data object AppStartNavigation : Route("appStartNavigation")
    data object OnBoardingScreen : Route("onBoardingScreen")

    data object SignInNavigation : Route("signInNavigation")
    data object SignInScreen : Route("signInScreen")

    data object MainNavigation : Route("mainNavigation")
    data object MainNavigator : Route("mainNavigator")
    data object HomeScreen : Route("homeScreen")
    data object SnapScreen : Route("snapScreen")
    data object TaskScreen : Route("taskScreen")
    data object FlashcardScreen : Route("flashcardScreen")
    data object HistoryScreen : Route("historyScreen")
    data object ProfileScreen : Route("profileScreen")
}