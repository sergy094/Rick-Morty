package com.sergiospinola.feature.home.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sergiospinola.common.designsystem.component.Loader
import com.sergiospinola.common.designsystem.theme.Grey
import com.sergiospinola.common.designsystem.theme.spacingS
import com.sergiospinola.common.designsystem.theme.spacingXS
import com.sergiospinola.common.designsystem.theme.spacingXXS
import com.sergiospinola.data.model.CharacterListData
import com.sergiospinola.feature.home.R
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModelInterface = hiltViewModel<HomeScreenViewModel>(),
) {
    val uiState by viewModel.homeScreenUiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.navigation) {
        when (val navigation = uiState.navigation) {
            // TODO: include here all you navigations
            //is HomeScreenNavigation.NavigateToDetail -> {
            //    onNavigateToDetail(navigation.itemId)
            //}
            else -> {
                // Does nothing
            }
        }
        viewModel.handle(HomeScreenEvent.DidNavigate)
    }

    Loader(viewModel = viewModel)
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(
                    PaddingValues(
                        start = spacingS(),
                        end = spacingS(),
                        top = padding.calculateTopPadding(),
                        bottom = padding.calculateBottomPadding()
                    )
                )
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(spacingXS()),
            ) {
                items(uiState.characters.size) { index ->
                    CharacterCardComponent(uiState.characters[index])
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                if (uiState.previousPage != null) {
                    IconButton(
                        onClick = {
                            viewModel.handle(HomeScreenEvent.OnPreviousPressed)
                        }
                    ) {
                        Image(
                            painter = painterResource(com.sergiospinola.common.R.drawable.ic_arrow_circle_left),
                            contentDescription = "",
                            Modifier.background(Color.White)
                        )
                    }
                }
                Spacer(
                    modifier = Modifier.weight(1f)
                )
                if (uiState.nextPage != null) {
                    IconButton(
                        onClick = {
                            viewModel.handle(HomeScreenEvent.OnNextPressed)
                        }
                    ) {
                        Image(
                            painter = painterResource(com.sergiospinola.common.R.drawable.ic_arrow_circle_right),
                            contentDescription = "",
                            Modifier.background(Color.White)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CharacterCardComponent(character: CharacterListData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, Grey)
    ) {
        Box(
            modifier = Modifier
                .background(Color.White, RoundedCornerShape(spacingXXS()))
                .padding(horizontal = spacingS(), vertical = spacingS())
                .fillMaxWidth()
        ) {
            Text(character.name)
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    HomeScreen(
        viewModel = composePreviewViewModel
    )
}

private val composePreviewViewModel by lazy {
    object : HomeScreenViewModelInterface {
        // Outputs
        override val homeScreenUiState = MutableStateFlow(HomeScreenUiState())
        override val loading = MutableStateFlow(false)
        override val error = MutableStateFlow(null)

        // Inputs
        override fun closeError() {}
        override fun handle(event: HomeScreenEvent) {}
    }
}