package com.sergiospinola.feature.home.ui

import com.sergiospinola.common.base.BaseViewModel
import com.sergiospinola.common.base.BaseViewModelInterface
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
        }
    }

    init {
        launchWithErrorWrapper {
            repository.getAllCharacters()
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
    val yourData: String? = null,
    val navigation: HomeScreenNavigation? = null,
)

sealed interface HomeScreenNavigation {}

sealed interface HomeScreenEvent {
    object DidNavigate : HomeScreenEvent
}