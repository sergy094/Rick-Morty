package com.sergiospinola.feature.detail.ui

import androidx.lifecycle.SavedStateHandle
import com.sergiospinola.common.base.BaseViewModel
import com.sergiospinola.common.base.BaseViewModelInterface
import com.sergiospinola.data.model.CharacterData
import com.sergiospinola.data.model.EpisodeData
import com.sergiospinola.data.model.LocationData
import com.sergiospinola.data.repository.APIRepository
import com.sergiospinola.feature.detail.navigation.ID_PARAM
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

interface DetailViewModelInterface : BaseViewModelInterface {
    // Outputs
    val detailUiState: StateFlow<DetailUiState>

    // Inputs
    fun handle(event: DetailEvent)
}

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: APIRepository,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel(), DetailViewModelInterface {

    private val characterId = savedStateHandle.get<Int>(ID_PARAM)

    private val _detailUiState = MutableStateFlow(DetailUiState())
    override val detailUiState = _detailUiState.asStateFlow()

    override fun handle(event: DetailEvent) {
        when (event) {
            DetailEvent.DidNavigate -> didNavigate()
            DetailEvent.OnBackPressed -> navigateBack()
        }
    }

    init {
        getData()
    }

    private fun getData() {
        characterId?.let { characterId ->
            launchWithErrorWrapper {
                val character = repository.getCharacter(characterId)
                val episodes = repository.getEpisode(character.episode)
                _detailUiState.update {
                    it.copy(
                        characterData = character,
                        episodesData = episodes
                    )
                }
            }
        }
    }

    private fun navigateBack() {
        _detailUiState.update {
            it.copy(
                navigation = DetailNavigation.NavigateBack
            )
        }
    }

    private fun didNavigate() {
        if (_detailUiState.value.navigation != null) {
            _detailUiState.update {
                it.copy(
                    navigation = null,
                )
            }
        }
    }
}

data class DetailUiState(
    val characterData: CharacterData? = null,
    val episodesData: List<EpisodeData>? = null,
    val navigation: DetailNavigation? = null,
)

sealed interface DetailNavigation {
    object NavigateBack : DetailNavigation
}

sealed interface DetailEvent {
    object OnBackPressed : DetailEvent
    object DidNavigate : DetailEvent
}