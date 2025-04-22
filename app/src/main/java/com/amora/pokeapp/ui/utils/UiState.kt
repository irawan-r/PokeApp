package com.amora.pokeapp.ui.utils

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable

sealed class UiState<out T> {
    object Idle : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    data class Success<out T>(val message: String? = null, val data: T? = null) : UiState<T>()
    data class Empty(val message: String) : UiState<Nothing>()
    data class Error(val message: String) : UiState<Nothing>()
}

@Composable
fun <T> UiState<T>.onLoading(
    execute: @Composable () -> Unit
): UiState<T> = apply {
    if (this is UiState.Loading) {
        execute()
    }
}

@Composable
fun <T> UiState<T>.onError(
    execute: @Composable (String?) -> Unit
): UiState<T> = apply {
    if (this is UiState.Error) {
        execute(message)
    }
}

@Composable
fun <T> UiState<T>.onSuccess(
    execute: @Composable (T?) -> Unit
): UiState<T> = apply {
    if (this is UiState.Success) {
        execute(data)
    }
}

@Composable
fun <T> UiState<T>.onEmpty(
    execute: @Composable () -> Unit
): UiState<T> = apply {
    if (this is UiState.Empty) {
        execute.invoke()
    }
}

suspend fun <T> UiState<T>.showSnackbarIfNeeded(snackbarHostState: SnackbarHostState): T? {
    val message: String?
    val data: T?

    when (this) {
        is UiState.Success -> {
            message = this.message
            data = this.data
        }

        is UiState.Error -> {
            message = this.message
            data = null
        }

        is UiState.Empty -> {
            message = this.message
            data = null
        }

        else -> {
            message = null
            data = null
        }
    }

    if (!message.isNullOrBlank()) {
        snackbarHostState.showSnackbar(message)
    }

    return data
}