package com.sergiospinola.feature.home.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Size
import com.sergiospinola.common.designsystem.component.Loader
import com.sergiospinola.common.designsystem.theme.Blue
import com.sergiospinola.common.designsystem.theme.Green
import com.sergiospinola.common.designsystem.theme.HeadlineSmall
import com.sergiospinola.common.designsystem.theme.spacingL
import com.sergiospinola.common.designsystem.theme.spacingM
import com.sergiospinola.common.designsystem.theme.spacingS
import com.sergiospinola.common.designsystem.theme.spacingXS
import com.sergiospinola.data.model.CharacterListData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

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
                        top = padding.calculateTopPadding(),
                        bottom = padding.calculateBottomPadding()
                    )
                )
        ) {
            val listState = rememberLazyListState()
            val coroutineScope = rememberCoroutineScope()

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = spacingS())
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(spacingXS()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Image(
                        painter = painterResource(com.sergiospinola.common.R.drawable.rick_and_morty),
                        contentDescription = ""
                    )
                    Spacer(modifier = Modifier.height(spacingL()))
                }
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
                            coroutineScope.launch {
                                listState.scrollToItem(0)
                            }
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
                            coroutineScope.launch {
                                listState.scrollToItem(0)
                            }
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
        shape = RoundedCornerShape(50),
        border = BorderStroke(3.dp, Blue),
        colors = CardDefaults.cardColors(
            containerColor = Green
        )
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = spacingS(), vertical = spacingS())
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(100.dp)
                        .border(2.dp, Color.Black, CircleShape)
                        .clip(CircleShape),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(character.image)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .crossfade(true)
                        .size(Size.ORIGINAL)
                        .build(),
                    contentDescription = "",
                )
                Spacer(modifier = Modifier.width(spacingM()))
                Text(
                    text = character.name,
                    textAlign = TextAlign.Center,
                    style = HeadlineSmall
                )
            }
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
        override val homeScreenUiState = MutableStateFlow(HomeScreenUiState(
            characters = listOf(
                CharacterListData(
                    id = 1,
                    name = "Rick Sanchez",
                    image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
                ),
            )
        ))
        override val loading = MutableStateFlow(false)
        override val error = MutableStateFlow(null)

        // Inputs
        override fun closeError() {}
        override fun handle(event: HomeScreenEvent) {}
    }
}