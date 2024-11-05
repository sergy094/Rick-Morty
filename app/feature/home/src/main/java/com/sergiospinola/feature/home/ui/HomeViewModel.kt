package com.sergiospinola.feature.home.ui

import com.sergiospinola.common.base.BaseViewModel
import com.sergiospinola.common.base.BaseViewModelInterface
import com.sergiospinola.data.model.CharacterData
import com.sergiospinola.data.model.CharacterGenderTypeData
import com.sergiospinola.data.model.CharacterStatusTypeData
import com.sergiospinola.data.repository.APIRepository
import com.sergiospinola.feature.home.model.FilterUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

interface HomeScreenViewModelInterface : BaseViewModelInterface {
    // Outputs
    val homeScreenUiState: StateFlow<HomeScreenUiState>

    // Inputs
    fun handle(event: HomeScreenEvent)
}

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val repository: APIRepository
) : BaseViewModel(), HomeScreenViewModelInterface {

    private val _homeScreenUiState = MutableStateFlow(HomeScreenUiState())
    override val homeScreenUiState = _homeScreenUiState.asStateFlow()

    override fun handle(event: HomeScreenEvent) {
        when (event) {
            HomeScreenEvent.DidNavigate -> didNavigate()
            is HomeScreenEvent.OnNextPressed -> {
                if (_homeScreenUiState.value.appliedFilters.hasFilterApplied()) {
                    getFilteredCharacters(_homeScreenUiState.value.nextPage)
                } else {
                    getCharacters(_homeScreenUiState.value.nextPage)
                }
            }
            is HomeScreenEvent.OnPreviousPressed -> {
                if (_homeScreenUiState.value.appliedFilters.hasFilterApplied()) {
                    getFilteredCharacters(_homeScreenUiState.value.previousPage)
                } else {
                getCharacters(_homeScreenUiState.value.previousPage)
                    }
            }
            is HomeScreenEvent.OnCharacterPressed -> navigateToDetail(event.characterId)
            is HomeScreenEvent.OnFilterChanged -> updateFilter(event.filterType, event.filterValue)
            HomeScreenEvent.OnApplyFiltersPressed -> getFilteredCharacters()
            HomeScreenEvent.OnClearFiltersPressed -> clearFilters()
        }
    }

    init {
        getCharacters()
    }

    private fun getPageFromUrl(url: String?) =
        url?.substringAfter("page=")
            ?.substringBefore("&")?.toIntOrNull()

    private fun getCharacters(page: Int? = null) {
        launchWithErrorWrapper {
            val charactersResponse = repository.getAllCharacters(page)
            val characters = charactersResponse.results
            val nextPage = getPageFromUrl(charactersResponse.info.next)
            val previousPage = getPageFromUrl(charactersResponse.info.prev)
            _homeScreenUiState.update {
                it.copy(
                    characters = characters,
                    nextPage = nextPage,
                    previousPage = previousPage
                )
            }
        }
    }

    private fun getFilteredCharacters(page: Int? = null) {
        launchWithErrorWrapper {
            val filteredCharactersResponse = repository.getFilteredCharacters(
                page = page,
                name = _homeScreenUiState.value.appliedFilters.name,
                status = _homeScreenUiState.value.appliedFilters.status?.text,
                species = _homeScreenUiState.value.appliedFilters.species,
                type = _homeScreenUiState.value.appliedFilters.type,
                gender = _homeScreenUiState.value.appliedFilters.gender?.text
            )
            val characters = filteredCharactersResponse.results
            val nextPage = getPageFromUrl(filteredCharactersResponse.info.next)
            val previousPage = getPageFromUrl(filteredCharactersResponse.info.prev)
            _homeScreenUiState.update {
                it.copy(
                    characters = characters,
                    nextPage = nextPage,
                    previousPage = previousPage
                )
            }
        }
    }

    private fun navigateToDetail(characterId: Int) {
        _homeScreenUiState.update {
            it.copy(
                navigation = HomeScreenNavigation.NavigateToDetail(characterId)
            )
        }
    }

    private fun updateFilter(filterType: FilterTypeUIModel, filterValue: String?) {
        when (filterType) {
            FilterTypeUIModel.NAME -> {
                _homeScreenUiState.update {
                    it.copy(
                        appliedFilters = it.appliedFilters.copy(
                            name = filterValue
                        )
                    )
                }
            }


            FilterTypeUIModel.STATUS -> {
                _homeScreenUiState.update {
                    it.copy(
                        appliedFilters = it.appliedFilters.copy(
                            status = CharacterStatusTypeData.entries.firstOrNull { value ->
                                value.text == filterValue
                            }
                        )
                    )
                }
            }

            FilterTypeUIModel.SPECIES -> {
                _homeScreenUiState.update {
                    it.copy(
                        appliedFilters = it.appliedFilters.copy(
                            species = filterValue
                        )
                    )
                }
            }

            FilterTypeUIModel.TYPE -> {
                _homeScreenUiState.update {
                    it.copy(
                        appliedFilters = it.appliedFilters.copy(
                            type = filterValue
                        )
                    )
                }
            }

            FilterTypeUIModel.GENDER -> {
                _homeScreenUiState.update {
                    it.copy(
                        appliedFilters = it.appliedFilters.copy(
                            gender = CharacterGenderTypeData.entries.firstOrNull { value ->
                                value.text == filterValue
                            }
                        )
                    )
                }
            }

        }
    }

    private fun clearFilters() {
        FilterTypeUIModel.entries.forEach { type ->
            updateFilter(type, null)
        }
    }

    private fun didNavigate() {
        if (_homeScreenUiState.value.navigation != null) {
            _homeScreenUiState.update {
                it.copy(
                    navigation = null,
                )
            }
        }
    }
}

data class HomeScreenUiState(
    val characters: List<CharacterData> = emptyList(),
    val nextPage: Int? = null,
    val previousPage: Int? = null,
    val appliedFilters: FilterUIModel = FilterUIModel(),
    val navigation: HomeScreenNavigation? = null,
)

sealed interface HomeScreenNavigation {
    data class NavigateToDetail(val characterId: Int) : HomeScreenNavigation
}

sealed interface HomeScreenEvent {
    object OnNextPressed : HomeScreenEvent
    object OnPreviousPressed : HomeScreenEvent
    data class OnCharacterPressed(val characterId: Int) : HomeScreenEvent
    data class OnFilterChanged(val filterType: FilterTypeUIModel, val filterValue: String) : HomeScreenEvent
    object OnApplyFiltersPressed : HomeScreenEvent
    object OnClearFiltersPressed : HomeScreenEvent
    object DidNavigate : HomeScreenEvent
}

enum class FilterTypeUIModel {
    NAME,
    STATUS,
    GENDER,
    SPECIES,
    TYPE
}