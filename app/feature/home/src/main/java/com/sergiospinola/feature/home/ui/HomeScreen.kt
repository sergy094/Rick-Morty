@file:OptIn(ExperimentalFoundationApi::class)

package com.sergiospinola.feature.home.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedIconToggleButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Size
import com.sergiospinola.common.R
import com.sergiospinola.common.designsystem.component.Loader
import com.sergiospinola.common.designsystem.component.buttons.PrimaryButton
import com.sergiospinola.common.designsystem.theme.BackgroundAppColor
import com.sergiospinola.common.designsystem.theme.Blue
import com.sergiospinola.common.designsystem.theme.Green
import com.sergiospinola.common.designsystem.theme.HeadlineSmall
import com.sergiospinola.common.designsystem.theme.spacingM
import com.sergiospinola.common.designsystem.theme.spacingS
import com.sergiospinola.common.designsystem.theme.spacingXS
import com.sergiospinola.data.model.CharacterData
import com.sergiospinola.data.model.CharacterGenderTypeData
import com.sergiospinola.data.model.CharacterStatusTypeData
import com.sergiospinola.data.model.LocationData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModelInterface = hiltViewModel<HomeScreenViewModel>(),
    navigateToDetail: (Int) -> Unit
) {
    val uiState by viewModel.homeScreenUiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.navigation) {
        when (val navigation = uiState.navigation) {
            is HomeScreenNavigation.NavigateToDetail -> {
                navigateToDetail(navigation.characterId)
            }

            else -> {
                // Does nothing
            }
        }
        viewModel.handle(HomeScreenEvent.DidNavigate)
    }

    Loader(viewModel = viewModel)
    Scaffold { innerPadding ->
        val statusBarPadding = WindowInsets.statusBars.asPaddingValues()
        Column(
            modifier = Modifier
                .padding(
                    PaddingValues(
                        top = innerPadding.calculateTopPadding() + statusBarPadding.calculateTopPadding(),
                        bottom = innerPadding.calculateBottomPadding()
                    )
                )
        ) {
            val listState = rememberLazyListState()
            val coroutineScope = rememberCoroutineScope()
            var filterChecked by remember { mutableStateOf(false) }

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(spacingXS()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Spacer(modifier = Modifier.height(spacingXS()))
                    Image(
                        painter = painterResource(R.drawable.logo_img),
                        contentDescription = ""
                    )
                    Spacer(modifier = Modifier.height(spacingXS()))
                }
                stickyHeader {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(BackgroundAppColor)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = spacingS())
                        ) {
                            if (uiState.previousPage != null) {
                                IconButton(
                                    onClick = {
                                        coroutineScope.launch {
                                            listState.scrollToItem(0)
                                        }
                                        viewModel.handle(HomeScreenEvent.OnPreviousPressed)
                                    },
                                ) {
                                    Image(
                                        modifier = Modifier
                                            .size(100.dp)
                                            .background(Color.Black),
                                        painter = painterResource(id = R.drawable.ic_arrow_circle_left),
                                        contentDescription = "",
                                        colorFilter = ColorFilter.tint(Color.White)
                                    )
                                }
                            }
                            Spacer(
                                modifier = Modifier.width(spacingXS())
                            )
                            OutlinedIconToggleButton(
                                checked = filterChecked,
                                onCheckedChange = { filterChecked = it },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Filter")
                            }
                            Spacer(
                                modifier = Modifier.width(spacingXS())
                            )
                            if (uiState.nextPage != null) {
                                IconButton(
                                    onClick = {
                                        coroutineScope.launch {
                                            listState.scrollToItem(0)
                                        }
                                        viewModel.handle(HomeScreenEvent.OnNextPressed)
                                    },
                                ) {
                                    Image(
                                        modifier = Modifier
                                            .size(100.dp)
                                            .background(Color.Black),
                                        painter = painterResource(id = R.drawable.ic_arrow_circle_right),
                                        contentDescription = "",
                                        colorFilter = ColorFilter.tint(Color.White)
                                    )
                                }
                            }
                        }
                        AnimatedVisibility(filterChecked) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = spacingS())
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        modifier = Modifier.width(100.dp),
                                        text = "Name"
                                    )
                                    TextField(
                                        value = "",
                                        onValueChange = {},
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .weight(1f)
                                    )
                                }
                                Spacer(
                                    modifier = Modifier.height(spacingXS())
                                )
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        modifier = Modifier.width(100.dp),
                                        text = "Status"
                                    )
                                    TextField(
                                        value = "",
                                        onValueChange = {},
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .weight(1f)
                                    )
                                }
                                Spacer(
                                    modifier = Modifier.height(spacingXS())
                                )
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        modifier = Modifier.width(100.dp),
                                        text = "Species"
                                    )
                                    TextField(
                                        value = "",
                                        onValueChange = {},
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .weight(1f)
                                    )
                                }
                                Spacer(
                                    modifier = Modifier.height(spacingXS())
                                )
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        modifier = Modifier.width(100.dp),
                                        text = "Type"
                                    )
                                    TextField(
                                        value = "",
                                        onValueChange = {},
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .weight(1f)
                                    )
                                }
                                Spacer(
                                    modifier = Modifier.height(spacingXS())
                                )
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        modifier = Modifier.width(100.dp),
                                        text = "Gender"
                                    )
                                    TextField(
                                        value = "",
                                        onValueChange = {},
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .weight(1f)
                                    )
                                }
                                PrimaryButton(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = spacingS()),
                                    text = "Aplicar",
                                    onClick = {}
                                )
                            }
                        }
                    }

                }
                items(uiState.characters.size) { index ->
                    CharacterCardComponent(
                        character = uiState.characters[index],
                        onClick = {
                            viewModel.handle(HomeScreenEvent.OnCharacterPressed(uiState.characters[index].id))
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun CharacterCardComponent(character: CharacterData, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = spacingS()),
        shape = RoundedCornerShape(50),
        border = BorderStroke(3.dp, Blue),
        colors = CardDefaults.cardColors(
            containerColor = Green
        ),
        onClick = onClick
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
                        .size(dimensionResource(id = R.dimen.avatar_list_size))
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
                    placeholder = painterResource(id = R.drawable.empty_avatar_img)
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
        viewModel = composePreviewViewModel,
        navigateToDetail = {}
    )
}

private val composePreviewViewModel by lazy {
    object : HomeScreenViewModelInterface {
        // Outputs
        override val homeScreenUiState = MutableStateFlow(
            HomeScreenUiState(
                characters = listOf(
                    CharacterData(
                        id = 1,
                        name = "Rick Sanchez",
                        image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                        status = CharacterStatusTypeData.ALIVE,
                        species = "Human",
                        type = "",
                        gender = CharacterGenderTypeData.MALE,
                        origin = LocationData(
                            name = "Earth (C-137)",
                            url = "https://rickandmortyapi.com/api/location/1"
                        ),
                        location = LocationData(
                            name = "Earth (Replacement Dimension)",
                            url = "https://rickandmortyapi.com/api/location/20"
                        ),
                        episode = listOf(),
                        url = "https://rickandmortyapi.com/api/character/1",
                        created = ""
                    ),
                )
            )
        )
        override val loading = MutableStateFlow(false)
        override val error = MutableStateFlow(null)

        // Inputs
        override fun closeError() {}
        override fun handle(event: HomeScreenEvent) {}
    }
}