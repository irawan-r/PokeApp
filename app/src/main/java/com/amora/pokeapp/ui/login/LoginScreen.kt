package com.amora.pokeapp.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amora.pokeapp.repository.model.UserAccount
import com.amora.pokeapp.ui.main.AuthViewModel
import com.amora.pokeapp.ui.utils.UiState
import com.amora.pokeapp.ui.utils.showSnackbarIfNeeded

@Composable
fun LoginScreen(
    snackbarHostState: SnackbarHostState,
    viewModel: AuthViewModel,
    onLoginSuccess: (Boolean) -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val loginState: UiState<String> by viewModel.loginState.collectAsStateWithLifecycle(initialValue = UiState.Idle)

    LaunchedEffect(loginState) {
        onLoginSuccess(loginState is UiState.Success)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LoginForm(
            loginState = loginState,
            onLoginClick = { name, password ->
                val userData = UserAccount(name, password)
                viewModel.loginUser(userData)
            },
            snackbarHostState = snackbarHostState,
            navigateToRegister = onNavigateToRegister
        )

        if (loginState is UiState.Loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
fun LoginForm(
    loginState: UiState<String>,
    onLoginClick: (String, String) -> Unit,
    navigateToRegister: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    var tempName by remember { mutableStateOf("") }
    var tempPassword by remember { mutableStateOf("") }

    LaunchedEffect(loginState) {
        loginState.showSnackbarIfNeeded(snackbarHostState)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val nameFieldEmpty =
            (loginState is UiState.Error || loginState is UiState.Empty) && tempName.isBlank()
        val passwordFieldEmpty =
            (loginState is UiState.Error || loginState is UiState.Empty) && tempPassword.isBlank()

        Text("Welcome to Portal Pokemon", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        UsernameField(onNameChange = { tempName = it }, isError = nameFieldEmpty)

        PasswordField(onPasswordChange = { tempPassword = it }, isError = passwordFieldEmpty)

        LoginButton(
            onClick = { onLoginClick(tempName, tempPassword) },
            enabled = loginState != UiState.Loading,
            isLoading = loginState == UiState.Loading
        )

        TextButton(
            onClick = navigateToRegister,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text("Don't have an account? Register")
        }
    }
}

@Composable
private fun UsernameField(onNameChange: (String) -> Unit, isError: Boolean) {
    var name by rememberSaveable { mutableStateOf("") }
    OutlinedTextField(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        value = name,
        onValueChange = {
            name = it
            onNameChange(it)
        },
        label = { Text("Username") },
        singleLine = true,
        isError = isError,
        supportingText = { if (isError) Text("Field is Empty", color = Color.LightGray) }
    )
}

@Composable
private fun PasswordField(onPasswordChange: (String) -> Unit, isError: Boolean) {
    var password by rememberSaveable { mutableStateOf("") }
    OutlinedTextField(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        value = password,
        onValueChange = {
            password = it
            onPasswordChange(it)
        },
        label = { Text("Password") },
        visualTransformation = PasswordVisualTransformation(),
        singleLine = true,
        isError = isError,
        supportingText = { if (isError) Text("Field is Empty", color = Color.LightGray) }
    )
}

@Composable
private fun LoginButton(onClick: () -> Unit, enabled: Boolean, isLoading: Boolean) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
    ) {
        Text(if (isLoading) "Logging in..." else "Login")
    }
}

@Composable
fun PokeSnackbar(snackbarHostState: SnackbarHostState) {
    Box(modifier = Modifier.fillMaxSize()) {
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginForm(
        loginState = UiState.Idle,
        onLoginClick = { _, _ -> },
        navigateToRegister = {},
        snackbarHostState = SnackbarHostState()
    )
}