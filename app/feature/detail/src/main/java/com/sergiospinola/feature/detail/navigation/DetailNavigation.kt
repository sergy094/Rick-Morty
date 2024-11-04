package com.sergiospinola.feature.detail.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sergiospinola.feature.detail.ui.DetailScreen

internal const val DETAIL_NAVIGATION_ROUTE = "detail_navigation_route"
internal const val ID_PARAM = "characterId"

fun NavController.navigateToDetail(characterId: Int) {
    this.navigate("${DETAIL_NAVIGATION_ROUTE}/$characterId")
}

fun NavGraphBuilder.detailScreen(
    navController: NavController,
) {
    composable(
        route = "$DETAIL_NAVIGATION_ROUTE/{$ID_PARAM}",
        arguments = listOf(
            navArgument(ID_PARAM) { type = NavType.IntType }
        )
    ) {
        DetailScreen(
            navigateBack = {
                navController.popBackStack()
            }
        )
    }
}