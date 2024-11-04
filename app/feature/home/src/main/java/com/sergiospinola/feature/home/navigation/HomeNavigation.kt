package com.sergiospinola.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sergiospinola.feature.home.ui.HomeScreen

const val HOME_NAVIGATION_ROUTE = "home_navigation_route"

fun NavController.navigateToHome() {
    this.navigate(HOME_NAVIGATION_ROUTE)
}

fun NavGraphBuilder.homeScreen(
    navController: NavController,
    navigateToDetail: (Int) -> Unit
) {
    composable(HOME_NAVIGATION_ROUTE) {
        HomeScreen(
            navigateToDetail = navigateToDetail

        )
    }
}