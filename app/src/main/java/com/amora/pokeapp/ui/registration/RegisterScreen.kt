package com.amora.pokeapp.ui.registration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amora.pokeapp.repository.model.UserAccount
import com.amora.pokeapp.ui.login.PasswordField
import com.amora.pokeapp.ui.login.UsernameField
import com.amora.pokeapp.ui.main.AuthViewModel
import com.amora.pokeapp.ui.utils.UiState
import com.amora.pokeapp.ui.utils.showSnackbarIfNeeded

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    snackbarHostState: SnackbarHostState,
    onRegisterSuccess: (Boolean) -> Unit,
) {
    val registerState: UiState<String> by viewModel.regisState.collectAsStateWithLifecycle(
        initialValue = UiState.Idle
    )

    Box(modifier = Modifier.fillMaxSize()) {
        LaunchedEffect(registerState) {
            onRegisterSuccess(registerState is UiState.Success)
        }

        RegisterForm(
            viewModel = viewModel,
            regisState = registerState,
            snackbarHostState = snackbarHostState
        )

        if (registerState is UiState.Loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
private fun RegisterForm(
    viewModel: AuthViewModel,
    regisState: UiState<String>,
    snackbarHostState: SnackbarHostState
) {
    LaunchedEffect(regisState) {
        regisState.showSnackbarIfNeeded(snackbarHostState)
    }

    var tempName by rememberSaveable { mutableStateOf("") }

    var tempPassword by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val nameFieldEmpty =
            (regisState is UiState.Error || regisState is UiState.Empty) && tempName.isBlank()
        val passwordFieldEmpty =
            (regisState is UiState.Error || regisState is UiState.Empty) && tempPassword.isBlank()
        Text("Register", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        UsernameField(
            onNameChange = {
                tempName = it
            },
            isError = nameFieldEmpty
        )
        PasswordField(
            onPasswordChange = {
                tempPassword = it
            },
            isError = passwordFieldEmpty
        )
        Button(
            onClick = {
                viewModel.registerUser(UserAccount(tempName, tempPassword))
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = regisState != UiState.Loading
        ) {
            Text("Register")
        }
    }
}