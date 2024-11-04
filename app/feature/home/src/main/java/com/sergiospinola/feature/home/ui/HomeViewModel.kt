package com.sergiospinola.feature.home.ui

import com.sergiospinola.common.base.BaseViewModel
import com.sergiospinola.common.base.BaseViewModelInterface
import com.sergiospinola.data.model.CharacterData
import com.sergiospinola.data.repository.APIRepository
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
            HomeScreenEvent.OnNextPressed -> getCharacters(_homeScreenUiState.value.nextPage)
            HomeScreenEvent.OnPreviousPressed -> getCharacters(_homeScreenUiState.value.previousPage)
            is HomeScreenEvent.OnCharacterPressed -> navigateToDetail(event.characterId)
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

    private fun navigateToDetail(characterId: Int) {
        _homeScreenUiState.update {
            it.copy(
                navigation = HomeScreenNavigation.NavigateToDetail(characterId)
            )
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
    val navigation: HomeScreenNavigation? = null,
)

sealed interface HomeScreenNavigation {
    data class NavigateToDetail(val characterId: Int) : HomeScreenNavigation
}

sealed interface HomeScreenEvent {
    object OnNextPressed : HomeScreenEvent
    object OnPreviousPressed : HomeScreenEvent
    data class OnCharacterPressed(val characterId: Int) : HomeScreenEvent
    object DidNavigate : HomeScreenEvent
}