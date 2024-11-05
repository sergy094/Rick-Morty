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

const val RICK_ID = 1
const val EASTER_EGG_COUNT = 5

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
            DetailEvent.OnAvatarPressed -> increaseClickCount()
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
                        episodesData = episodes,
                        isEasterEggEnabled = characterId == RICK_ID,
                    )
                }
            }
        }
    }

    private fun increaseClickCount() {
        val updatedCount = _detailUiState.value.clickCount + 1
        if (updatedCount < EASTER_EGG_COUNT) {
            _detailUiState.update {
                it.copy(
                    clickCount = updatedCount
                )
            }
        } else {
            playEasterEgg()
            _detailUiState.update {
                it.copy(
                    clickCount = 0
                )
            }
        }
    }

    private fun playEasterEgg() {
        _detailUiState.update {
            it.copy(
                mustPlayEasterEgg = true
            )
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
    val isEasterEggEnabled: Boolean = false,
    val clickCount: Int = 0,
    val mustPlayEasterEgg: Boolean = false,
    val navigation: DetailNavigation? = null,
)

sealed interface DetailNavigation {
    object NavigateBack : DetailNavigation
}

sealed interface DetailEvent {
    object OnAvatarPressed : DetailEvent
    object OnBackPressed : DetailEvent
    object DidNavigate : DetailEvent
}