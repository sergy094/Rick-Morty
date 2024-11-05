package com.sergiospinola.feature.detail.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.sergiospinola.common.designsystem.component.appbar.AppBar
import com.sergiospinola.common.designsystem.theme.Blue
import com.sergiospinola.common.designsystem.theme.BodyLarge
import com.sergiospinola.common.designsystem.theme.BodyMedium
import com.sergiospinola.common.designsystem.theme.BodySmall
import com.sergiospinola.common.designsystem.theme.HeadlineLarge
import com.sergiospinola.common.designsystem.theme.HeadlineSmall
import com.sergiospinola.common.designsystem.theme.PrimaryColor
import com.sergiospinola.common.designsystem.theme.spacingM
import com.sergiospinola.common.designsystem.theme.spacingS
import com.sergiospinola.common.designsystem.theme.spacingXS
import com.sergiospinola.data.model.CharacterData
import com.sergiospinola.data.model.CharacterGenderTypeData
import com.sergiospinola.data.model.CharacterStatusTypeData
import com.sergiospinola.data.model.LocationData
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun DetailScreen(
    viewModel: DetailViewModelInterface = hiltViewModel<DetailViewModel>(),
    navigateBack: () -> Unit,
) {
    val uiState by viewModel.detailUiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.navigation) {
        when (val navigation = uiState.navigation) {
            is DetailNavigation.NavigateBack -> {
                navigateBack()
            }

            else -> {
                // Does nothing
            }
        }
        viewModel.handle(DetailEvent.DidNavigate)
    }

    Loader(viewModel)
    Scaffold(
        topBar = {
            AppBar(
                navigationIcon = {
                    IconButton(onClick = { viewModel.handle(DetailEvent.OnBackPressed) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = null
                        )
                    }
                },
                title = R.string.detail_title_text
            )
        }
    ) { innerPadding ->
        val statusBarPadding = WindowInsets.statusBars.asPaddingValues()
        val expandedState = remember { mutableStateOf(false) }

        if (!viewModel.loading.collectAsStateWithLifecycle().value) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = innerPadding.calculateTopPadding() + statusBarPadding.calculateTopPadding(),
                        bottom = innerPadding.calculateBottomPadding(),
                        start = spacingM(),
                        end = spacingM()
                    )
                    .clip(RoundedCornerShape(30.dp))
                    .border(3.dp, Blue, RoundedCornerShape(30.dp))
                    .background(PrimaryColor)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = spacingM()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Spacer(modifier = Modifier.height(spacingM()))
                            AsyncImage(
                                modifier = Modifier
                                    .size(dimensionResource(id = R.dimen.avatar_detail_size))
                                    .border(2.dp, Color.Black, RectangleShape),
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(uiState.characterData?.image)
                                    .diskCachePolicy(CachePolicy.ENABLED)
                                    .memoryCachePolicy(CachePolicy.ENABLED)
                                    .crossfade(true)
                                    .size(Size.ORIGINAL)
                                    .build(),
                                contentDescription = "",
                                placeholder = painterResource(id = R.drawable.empty_avatar_img)
                            )
                            Spacer(modifier = Modifier.height(spacingM()))
                            Text(
                                text = uiState.characterData?.name ?: "",
                                style = HeadlineLarge
                            )
                            DetailField(
                                title = stringResource(R.string.detail_status_text),
                                value = uiState.characterData?.status?.text ?: ""
                            )
                            DetailField(
                                title = stringResource(R.string.detail_species_text),
                                value = uiState.characterData?.species ?: ""
                            )
                            if (uiState.characterData?.type?.isNotEmpty() == true) {
                                DetailField(
                                    title = stringResource(R.string.detail_type_text),
                                    value = uiState.characterData?.type ?: ""
                                )
                            }
                            DetailField(
                                title = stringResource(R.string.detail_status_text),
                                value = uiState.characterData?.gender?.text ?: ""
                            )
                            DetailField(
                                title = stringResource(R.string.detail_origin_text),
                                value = uiState.characterData?.origin?.name ?: ""
                            )
                            DetailField(
                                title = stringResource(R.string.detail_location_text),
                                value = uiState.characterData?.location?.name ?: ""
                            )
                            EpisodesTitle(
                                title = stringResource(R.string.detail_episodes_text),
                                expanded = expandedState,
                                onExpandChange = { newExpanded ->
                                    expandedState.value = newExpanded
                                }
                            )
                        }
                    }
                    if (expandedState.value) {
                        items(uiState.episodesData?.size ?: 0) { index ->
                            val episode = uiState.episodesData?.get(index)
                            Spacer(modifier = Modifier.height(spacingXS()))
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(3.dp, Color.Black, RectangleShape)
                                    .background(Color.White)
                                    .padding(spacingS()),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    text = "${episode?.episode ?: ""} - ${episode?.name ?: ""}",
                                    style = BodyMedium
                                )
                                Text(
                                    text =
                                    episode?.air_date ?: "",
                                    style = BodySmall
                                )
                            }
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(spacingM()))
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailField(
    title: String,
    value: String,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        HorizontalDivider(
            modifier = Modifier.padding(vertical = spacingM()),
            thickness = 1.dp,
            color = Color.Black
        )
        Text(
            text = title,
            style = HeadlineSmall,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(spacingXS()))
        Text(
            text = value,
            style = BodyLarge,
            color = Color.Black
        )

    }
}

@Composable
private fun EpisodesTitle(
    title: String,
    expanded: MutableState<Boolean>,
    onExpandChange: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        HorizontalDivider(
            modifier = Modifier.padding(vertical = spacingM()),
            thickness = 1.dp,
            color = Color.Black
        )
        Row(
            modifier = Modifier
                .clickable { onExpandChange(!expanded.value) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = HeadlineSmall,
                color = Color.Gray
            )
            Icon(
                imageVector = if (expanded.value) {
                    Icons.Filled.KeyboardArrowUp
                } else {
                    Icons.Filled.KeyboardArrowDown
                },
                contentDescription = "",
                tint = Color.Gray
            )
        }
    }
}

@Preview
@Composable
private fun DetailScreenPreview() {
    DetailScreen(
        viewModel = composePreviewViewModel,
        navigateBack = {}
    )
}

private val composePreviewViewModel by lazy {
    object : DetailViewModelInterface {
        // Outputs
        override val detailUiState = MutableStateFlow(
            DetailUiState(
                characterData = CharacterData(
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
        override val loading = MutableStateFlow(false)
        override val error = MutableStateFlow(null)

        // Inputs
        override fun closeError() {}
        override fun handle(event: DetailEvent) {}
    }
}