@file:OptIn(ExperimentalFoundationApi::class, ExperimentalFoundationApi::class)

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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.decode.ImageDecoderDecoder
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Size
import com.sergiospinola.common.R
import com.sergiospinola.common.designsystem.component.Loader
import com.sergiospinola.common.designsystem.component.buttons.CustomToggleButton
import com.sergiospinola.common.designsystem.component.buttons.PrimaryButton
import com.sergiospinola.common.designsystem.component.buttons.PrimaryOutlinedButton
import com.sergiospinola.common.designsystem.component.dropdown.CustomFilterDropDownMenu
import com.sergiospinola.common.designsystem.component.dropdown.NO_SELECTION
import com.sergiospinola.common.designsystem.component.textfield.CustomFilterOutlinedTextField
import com.sergiospinola.common.designsystem.theme.BackgroundAppColor
import com.sergiospinola.common.designsystem.theme.Black
import com.sergiospinola.common.designsystem.theme.HeadlineSmall
import com.sergiospinola.common.designsystem.theme.PrimaryColor
import com.sergiospinola.common.designsystem.theme.Red
import com.sergiospinola.common.designsystem.theme.SecondaryColor
import com.sergiospinola.common.designsystem.theme.spacingL
import com.sergiospinola.common.designsystem.theme.spacingM
import com.sergiospinola.common.designsystem.theme.spacingS
import com.sergiospinola.common.designsystem.theme.spacingXS
import com.sergiospinola.data.model.CharacterData
import com.sergiospinola.data.model.CharacterGenderTypeData
import com.sergiospinola.data.model.CharacterStatusTypeData
import com.sergiospinola.feature.home.model.FilterUIModel
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
            val filterChecked = remember { mutableStateOf(false) }

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(spacingXS()))
                        Image(
                            modifier = Modifier
                                .wrapContentWidth()
                                .height(60.dp)
                                .clip(RoundedCornerShape(15)),
                            painter = painterResource(R.drawable.logo_img),
                            contentDescription = "",
                        )
                        Spacer(modifier = Modifier.height(spacingXS()))
                    }
                }
                stickyHeader {
                    Box(
                        modifier = Modifier
                            .clip(GenericShape { size, _ ->
                                lineTo(size.width, 0f)
                                lineTo(size.width, Float.MAX_VALUE)
                                lineTo(0f, Float.MAX_VALUE)
                            })
                            .shadow(8.dp)
                    ) {
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
                                IconButton(
                                    enabled = uiState.previousPage != null,
                                    onClick = {
                                        coroutineScope.launch {
                                            listState.scrollToItem(0)
                                        }
                                        viewModel.handle(HomeScreenEvent.OnPreviousPressed)
                                    },
                                ) {
                                    Image(
                                        modifier = Modifier.fillMaxSize(),
                                        painter = painterResource(id = R.drawable.ic_arrow_circle_left),
                                        contentDescription = "",
                                        colorFilter = if (uiState.previousPage != null) {
                                            ColorFilter.tint(Black)
                                        } else {
                                            ColorFilter.tint(Color.Transparent)
                                        }
                                    )
                                }
                                Spacer(
                                    modifier = Modifier.width(spacingXS())
                                )
                                CustomToggleButton(
                                    text = "Filter",
                                    checked = filterChecked.value,
                                    onCheckedChange = { filterChecked.value = it },
                                    modifier = Modifier.weight(1f)
                                )
                                Spacer(
                                    modifier = Modifier.width(spacingXS())
                                )
                                IconButton(
                                    enabled = uiState.nextPage != null,
                                    onClick = {
                                        coroutineScope.launch {
                                            listState.scrollToItem(0)
                                        }
                                        viewModel.handle(HomeScreenEvent.OnNextPressed)
                                    },
                                ) {
                                    Image(
                                        modifier = Modifier.fillMaxSize(),
                                        painter = painterResource(id = R.drawable.ic_arrow_circle_right),
                                        contentDescription = "",
                                        colorFilter = if (uiState.nextPage != null) {
                                            ColorFilter.tint(Black)
                                        } else {
                                            ColorFilter.tint(Color.Transparent)
                                        }
                                    )
                                }
                            }
                            AnimatedVisibility(filterChecked.value) {
                                FilterComponent(
                                    uiState = uiState,
                                    isExpanded = filterChecked,
                                    onFilterChanged = { type, value ->
                                        viewModel.handle(
                                            HomeScreenEvent.OnFilterChanged(
                                                type,
                                                value
                                            )
                                        )
                                    },
                                    onApplyFilters = {
                                        viewModel.handle(HomeScreenEvent.OnApplyFiltersPressed)
                                    },
                                    onClearFilters = {
                                        viewModel.handle(HomeScreenEvent.OnClearFiltersPressed)
                                    }
                                )
                            }
                        }
                    }
                }
                if (uiState.characters.isEmpty()
                    && uiState.appliedFilters.hasFilterApplied()
                ) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(spacingL())
                        ) {
                            EmptyFilteredCharacters()
                        }
                    }
                } else {
                    items(uiState.characters.size) { index ->
                        Column {
                            Spacer(modifier = Modifier.height(spacingS()))
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
    }
}

@Composable
private fun CharacterCardComponent(character: CharacterData, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = spacingS()),
        shape = RoundedCornerShape(25),
        border = BorderStroke(2.dp, SecondaryColor),
        colors = CardDefaults.cardColors(
            containerColor = PrimaryColor
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
                        .border(2.dp, SecondaryColor, RoundedCornerShape(25))
                        .clip(RoundedCornerShape(25)),
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

@Composable
private fun FilterComponent(
    uiState: HomeScreenUiState,
    isExpanded: MutableState<Boolean>,
    onFilterChanged: (FilterTypeUIModel, String) -> Unit,
    onApplyFilters: () -> Unit,
    onClearFilters: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(BackgroundAppColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacingS())
        ) {
            CustomFilterOutlinedTextField(
                textTitle = stringResource(R.string.filter_name_text),
                fieldValue = uiState.appliedFilters.name ?: "",
                fieldOnValueChange = { value ->
                    onFilterChanged(FilterTypeUIModel.NAME, value)
                },
                fieldKeyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() },
                    onNext = { focusManager.clearFocus() }
                ),
            )
            Spacer(
                modifier = Modifier.height(spacingXS())
            )
            CustomFilterDropDownMenu(
                textTitle = stringResource(R.string.filter_status_text),
                fieldValues = CharacterStatusTypeData.entries.map { it.text },
                fieldSelectedIndex = uiState.appliedFilters.status?.ordinal
                    ?: NO_SELECTION,
                fieldOnSelectionChanged = { value ->
                    onFilterChanged(
                        FilterTypeUIModel.STATUS,
                        CharacterStatusTypeData.entries[value].text
                    )
                },
            )
            Spacer(
                modifier = Modifier.height(spacingXS())
            )
            CustomFilterOutlinedTextField(
                textTitle = stringResource(R.string.filter_species_text),
                fieldValue = uiState.appliedFilters.species ?: "",
                fieldOnValueChange = { value ->
                    onFilterChanged(FilterTypeUIModel.SPECIES, value)
                },
                fieldKeyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() },
                    onNext = { focusManager.clearFocus() }
                ),
            )
            Spacer(
                modifier = Modifier.height(spacingXS())
            )
            CustomFilterOutlinedTextField(
                textTitle = stringResource(R.string.filter_type_text),
                fieldValue = uiState.appliedFilters.type ?: "",
                fieldOnValueChange = { value ->
                    onFilterChanged(FilterTypeUIModel.TYPE, value)
                },
                fieldKeyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() },
                    onNext = { focusManager.clearFocus() }
                ),
            )
            Spacer(
                modifier = Modifier.height(spacingXS())
            )
            CustomFilterDropDownMenu(
                textTitle = stringResource(R.string.filter_gender_text),
                fieldValues = CharacterGenderTypeData.entries.map { it.text },
                fieldSelectedIndex = uiState.appliedFilters.gender?.ordinal
                    ?: NO_SELECTION,
                fieldOnSelectionChanged = { value ->
                    onFilterChanged(
                        FilterTypeUIModel.GENDER,
                        CharacterGenderTypeData.entries[value].text
                    )
                },
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = spacingS()),
            ) {
                PrimaryOutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    text = stringResource(R.string.filter_clear_text),
                    color = Red,
                    onClick = {
                        onClearFilters()
                    }
                )
                Spacer(modifier = Modifier.width(spacingS()))
                PrimaryButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    text = stringResource(R.string.filter_apply_text),
                    onClick = {
                        isExpanded.value = false
                        onApplyFilters()
                    }
                )
            }
        }
    }
}

@Composable
fun EmptyFilteredCharacters() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = spacingS()),
        shape = RoundedCornerShape(25),
        border = BorderStroke(2.dp, SecondaryColor),
        colors = CardDefaults.cardColors(
            containerColor = PrimaryColor
        )
    ) {
        val context = LocalContext.current

        Column(
            modifier = Modifier
                .padding(horizontal = spacingM(), vertical = spacingM())
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.filter_no_results_text),
                textAlign = TextAlign.Center,
                style = HeadlineSmall
            )
            Spacer(modifier = Modifier.height(spacingS()))
            Image(
                modifier = Modifier
                    .border(2.dp, SecondaryColor, RoundedCornerShape(25))
                    .clip(RoundedCornerShape(25)),
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(context).data(data = R.drawable.dancing_rick_gif).apply(
                        block = {
                            size(Size.ORIGINAL)
                        }).build(),
                    imageLoader = ImageLoader.Builder(context)
                        .components {
                            add(ImageDecoderDecoder.Factory())
                        }
                        .build()
                ),
                contentDescription = null
            )
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

@Preview
@Composable
private fun FilterComponentPreview() {
    FilterComponent(
        uiState = HomeScreenUiState(),
        isExpanded = remember { mutableStateOf(true) },
        onFilterChanged = { _, _ -> },
        onApplyFilters = {},
        onClearFilters = {}
    )
}

private val composePreviewViewModel by lazy {
    object : HomeScreenViewModelInterface {
        // Outputs
        override val homeScreenUiState = MutableStateFlow(
            HomeScreenUiState(
                characters = emptyList(),
                appliedFilters = FilterUIModel(name = "Rick")
//                characters = listOf(
//                    CharacterData(
//                        id = 1,
//                        name = "Rick Sanchez",
//                        image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
//                        status = CharacterStatusTypeData.ALIVE,
//                        species = "Human",
//                        type = "",
//                        gender = CharacterGenderTypeData.MALE,
//                        origin = LocationData(
//                            name = "Earth (C-137)",
//                            url = "https://rickandmortyapi.com/api/location/1"
//                        ),
//                        location = LocationData(
//                            name = "Earth (Replacement Dimension)",
//                            url = "https://rickandmortyapi.com/api/location/20"
//                        ),
//                        episode = listOf(),
//                        url = "https://rickandmortyapi.com/api/character/1",
//                        created = ""
//                    ),
//                )
            )
        )
        override val loading = MutableStateFlow(false)
        override val error = MutableStateFlow(null)

        // Inputs
        override fun closeError() {}
        override fun handle(event: HomeScreenEvent) {}
    }
}