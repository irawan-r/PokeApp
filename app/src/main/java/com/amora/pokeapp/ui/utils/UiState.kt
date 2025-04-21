package com.amora.pokeapp.ui.utils

import androidx.compose.material3.SnackbarHostState

sealed class UiState {
    object Idle : UiState()
    object Loading : UiState()
    data class Success(val message: String) : UiState()
    data class Empty(val message: String): UiState()
    data class Error(val message: String) : UiState()
}

suspend fun UiState.showSnackbarIfNeeded(snackbarHostState: SnackbarHostState) {
    val message = when (this) {
        is UiState.Success -> this.message
        is UiState.Error -> this.message
        is UiState.Empty -> this.message
        else -> null
    }

    if (!message.isNullOrBlank()) {
        snackbarHostState.showSnackbar(message)
    }
}