package com.sergiospinola.rickmorty.ui.main

import com.sergiospinola.common.base.BaseViewModel
import com.sergiospinola.common.base.BaseViewModelInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

interface MainViewModelInterface: BaseViewModelInterface {
    val mainUiState: StateFlow<MainUiState>
}

@HiltViewModel
class MainViewModel @Inject constructor(
) : BaseViewModel(), MainViewModelInterface {

    private val _mainUiState = MutableStateFlow(MainUiState())
    override val mainUiState: StateFlow<MainUiState> = _mainUiState

}

data class MainUiState(
    val skipTutorial: Boolean = false,
)