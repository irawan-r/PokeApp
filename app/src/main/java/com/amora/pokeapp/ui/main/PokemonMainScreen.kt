package com.amora.pokeapp.ui.main

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.amora.pokeapp.repository.model.PokeMark
import com.amora.pokeapp.ui.NavScreen
import com.amora.pokeapp.ui.details.PokemonDetails
import com.amora.pokeapp.ui.login.LoginScreen
import com.amora.pokeapp.ui.login.PokeSnackbar
import com.amora.pokeapp.ui.posters.Posters
import com.amora.pokeapp.ui.registration.RegisterScreen
import com.amora.pokeapp.ui.search.SearchScreen
import com.amora.pokeapp.ui.splash.SplashScreen
import com.amora.pokeapp.ui.utils.orFalse
import com.amora.pokeapp.ui.utils.showInternetStatus
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun PokemonMainScreen(navController: NavHostController) {
    val colors = MaterialTheme.colorScheme
    val systemUiController = rememberSystemUiController()

    var statusBarColor by remember { mutableStateOf(colors.surfaceTint) }
    var navigationBarColor by remember { mutableStateOf(colors.surfaceTint) }

    val animatedStatusBarColor by animateColorAsState(
        targetValue = statusBarColor,
        animationSpec = tween(),
        label = "StatusBar"
    )

    val animatedNavigationBarColor by animateColorAsState(
        targetValue = statusBarColor,
        animationSpec = tween(),
        label = "NavigationBar"
    )

    val authViewModel: AuthViewModel = hiltViewModel()

    val mainViewModel: MainViewModel = hiltViewModel()

    val snackbarHostState = remember { SnackbarHostState() }

    val onlineState by mainViewModel.isOnline.collectAsState()

    var isSplashDone by remember { mutableStateOf(false) }

    val isLoggedIn by authViewModel.isLoggedIn.collectAsStateWithLifecycle(initialValue = false)

    LaunchedEffect(onlineState) {
        authViewModel.checkUserLoggedIn()
        snackbarHostState.showInternetStatus(onlineState)
    }

    Scaffold(
        snackbarHost = {
            if (isSplashDone) {
                PokeSnackbar(snackbarHostState)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = NavScreen.Splash.route,
            modifier = Modifier.padding(paddingValues),
        ) {
            composable(NavScreen.Splash.route) {
                SplashScreen(
                    onSplashFinished = { finished ->
                        val destination = if (isLoggedIn.orFalse()) {
                            NavScreen.MainRoot.route
                        } else {
                            NavScreen.AuthRoot.route
                        }
                        navController.navigate(destination) {
                            popUpTo(NavScreen.Splash.route) { inclusive = true }
                        }
                        isSplashDone = finished
                    }
                )
            }
            authGraph(navController, snackbarHostState)
            mainGraph(
                mainViewModel = mainViewModel,
                navController = navController,
                homeContent = {
                    LaunchedEffect(Unit) {
                        statusBarColor = colors.surfaceTint
                        navigationBarColor = colors.surfaceTint
                    }
                },
                detailContent = {
                    LaunchedEffect(Unit) {
                        statusBarColor = colors.surfaceTint
                        navigationBarColor = colors.surfaceTint
                    }
                }
            )
        }
    }
    LaunchedEffect(animatedStatusBarColor, animatedNavigationBarColor, isSplashDone) {
        systemUiController.setStatusBarColor(animatedStatusBarColor)
        if (isSplashDone) {
            systemUiController.setNavigationBarColor(animatedNavigationBarColor)
        }
    }
}

fun NavGraphBuilder.mainGraph(
    mainViewModel: MainViewModel,
    navController: NavController,
    homeContent: @Composable (() -> Unit),
    detailContent: @Composable (() -> Unit)
) {
    navigation(
        startDestination = NavScreen.Home.route,
        route = NavScreen.MainRoot.route
    ) {
        composable(NavScreen.Home.route) {
            Posters(
                viewModel = hiltViewModel(),
                selectedPoster = { data ->
                    val route = NavScreen.createRoute(data?.id ?: 0, data?.name.orEmpty())
                    navController.navigate(route)
                },
                selectSearch = {
                    navController.navigate(NavScreen.SearchPokemon.route)
                },
                logOutAction = {
                    mainViewModel.logOutUser()
                    navController.navigate(NavScreen.AuthRoot.route) {
                        popUpTo(NavScreen.MainRoot.route) { inclusive = true }
                    }
                }
            )
            homeContent.invoke()
        }

        composable(
            route = NavScreen.PokemonDetails.routeWithArgument,
            arguments = listOf(
                navArgument(NavScreen.PokemonDetails.id) { type = NavType.IntType },
                navArgument(NavScreen.PokemonDetails.name) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val pokeName = backStackEntry.arguments?.getString(NavScreen.PokemonDetails.name)
            val pokeId = backStackEntry.arguments?.getInt(NavScreen.PokemonDetails.id)

            PokemonDetails(
                poke = PokeMark(id = pokeId ?: 0, name = pokeName.orEmpty()),
                viewModel = hiltViewModel()
            ) {
                navController.navigateUp()
            }
            detailContent.invoke()
        }

        composable(NavScreen.SearchPokemon.route) {
            SearchScreen(viewModel = hiltViewModel()) {
                navController.navigateUp()
            }
        }
    }
}

fun NavGraphBuilder.authGraph(navController: NavController, snackbarHostState: SnackbarHostState) {
    navigation(
        startDestination = NavScreen.Login.route,
        route = NavScreen.AuthRoot.route
    ) {
        composable(NavScreen.Login.route) {
            LoginScreen(
                viewModel = hiltViewModel(),
                snackbarHostState = snackbarHostState,
                onLoginSuccess = { isLoggedIn ->
                    if (isLoggedIn) {
                        navController.navigate(NavScreen.MainRoot.route) {
                            popUpTo(NavScreen.AuthRoot.route) { inclusive = true }
                        }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(NavScreen.Registration.route)
                }
            )
        }

        composable(NavScreen.Registration.route) {
            RegisterScreen(
                viewModel = hiltViewModel(),
                snackbarHostState = snackbarHostState,
                onRegisterSuccess = { isSuccess ->
                    if (isSuccess) {
                        navController.navigateUp()
                    }
                }
            )
        }
    }
}
