package com.jskaleel.sangaelakkiyangal.ui.screens.aboutapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jskaleel.sangaelakkiyangal.ui.utils.mutableNavigationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AboutAppViewModel @Inject constructor() : ViewModel() {

    var navigation by mutableNavigationState<AboutAppNavigationState>()
        private set

    private val viewModelState = MutableStateFlow(AboutAppViewModelState())

    val uiState = viewModelState.map { it.toUiState() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = viewModelState.value.toUiState()
        )

    init {
        viewModelScope.launch {
            viewModelState.update {
                it.copy(loading = true)
            }
            delay(1000)
            viewModelState.update {
                it.copy(loading = false)
            }
        }
    }
}

private data class AboutAppViewModelState(
    val loading: Boolean = false,
) {
    fun toUiState(): AboutAppUiState {
        return if (loading) {
            AboutAppUiState.Loading
        } else {
            AboutAppUiState.Success
        }
    }
}


sealed interface AboutAppUiState {
    data object Loading : AboutAppUiState
    data object Success : AboutAppUiState
}

sealed interface AboutAppNavigationState {
    object Next : AboutAppNavigationState
}