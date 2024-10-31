package com.sergiospinola.rickmorty.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.sergiospinola.common.designsystem.theme.AppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun MainCompose(
    onExitApp: () -> Unit,
    viewModel: MainViewModelInterface = hiltViewModel<MainViewModel>(),
) {
    val navController = rememberNavController()
    val mainUiState by viewModel.mainUiState.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = "HOME_FEATURE_NAVIGATION_ROUTE"
    ) {
//        homeSection(
//            navController,
//            navigateToCreateProduct = {
//                navController.navigateToCreateProduct()
//            },
//            navigateToDetail = navController::navigateToDetail,
//            navigateToProfile = navController::navigateToProfile,
//            navigateToLogin = navController::navigateToLogin,
//            navigateToBecomePro = navController::navigateToBecomePro,
//            navigateToFilter = navController::navigateToFilter
//        )
    }
}

@Preview
@Composable
private fun MainRoutePreview() {
    AppTheme {
        MainCompose(
            onExitApp = {},
            viewModel = composePreviewViewModel
        )
    }
}

private val composePreviewViewModel by lazy {
    object : MainViewModelInterface {
        override val mainUiState: StateFlow<MainUiState>
            get() = MutableStateFlow(
                MainUiState()
            )

        override val loading = MutableStateFlow(false)
        override val error = MutableStateFlow(null)

        override fun closeError() {}
    }
}