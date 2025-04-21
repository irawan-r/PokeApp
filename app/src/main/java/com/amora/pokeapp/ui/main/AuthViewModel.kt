package com.amora.pokeapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amora.pokeapp.repository.AuthRepository
import com.amora.pokeapp.repository.model.UserAccount
import com.amora.pokeapp.ui.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepo: AuthRepository
) : ViewModel() {

    private val _isLoggedIn = MutableStateFlow<Boolean?>(false)
    val isLoggedIn: StateFlow<Boolean?> = _isLoggedIn.asStateFlow()

    private val _authState: MutableSharedFlow<UserAccount> = MutableSharedFlow()
    val loginState: Flow<UiState> = _authState.flatMapConcat { user ->
        flow {
            emit(UiState.Loading)
            val result = authRepo.login(user.name, user.pass)
            val state = when {
                user.name.isBlank() || user.pass.isBlank() -> UiState.Empty("Field is empty")
                result -> UiState.Success("Success login")
                else -> UiState.Error("Invalid credentials")
            }
            emit(state)
        }
    }

    val regisState: Flow<UiState> = _authState.flatMapConcat { user ->
        flow {
            emit(UiState.Loading)
            val result = authRepo.register(user.name, user.pass)
            val state = when {
                user.name.isBlank() || user.pass.isBlank() -> UiState.Empty("Field is empty")
                result -> UiState.Success("Success register")
                else -> UiState.Error("Invalid credentials")
            }
            emit(state)
        }
    }

    fun checkUserLoggedIn() {
        viewModelScope.launch {
            _isLoggedIn.emit(authRepo.isLoggedIn())
        }
    }


    fun loginUser(userAccount: UserAccount) {
        viewModelScope.launch {
            _authState.emit(userAccount)
        }
    }

    fun registerUser(userAccount: UserAccount) {
        viewModelScope.launch {
            _authState.emit(userAccount)
        }
    }
}