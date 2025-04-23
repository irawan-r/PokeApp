package com.amora.pokeapp.ui.posters

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.IconButton
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.amora.pokeapp.R
import com.amora.pokeapp.constant.HomeTab
import com.amora.pokeapp.repository.model.PokeMark
import com.amora.pokeapp.ui.about.AboutScreen
import com.amora.pokeapp.ui.main.MainViewModel


@Composable
fun Posters(
    viewModel: MainViewModel,
    selectedPoster: (PokeMark?) -> Unit,
    selectSearch: () -> Unit,
    logOutAction: () -> Unit
) {
    val selectedTab = HomeTab.getTabFromSource(viewModel.selectedTab.value)
    val tabs = HomeTab.values().toList()
    val scrollState = rememberScrollState()

    val bottomNavOffset by animateDpAsState(
        targetValue = if (scrollState.value > 100) 56.dp else 0.dp,
        animationSpec = tween(300), label = ""
    )

    val bottomNavAlpha by animateFloatAsState(
        targetValue = if (scrollState.value > 0) 0f else 1f,
        animationSpec = tween(300), label = ""
    )

    val bottomBarState = rememberSaveable { (mutableStateOf(true)) }

    MainScreen(
        bottomBarState = bottomBarState,
        bottomNavOffset = bottomNavOffset,
        bottomNavAlpha = bottomNavAlpha,
        tabs = tabs,
        viewModel = viewModel,
        selectedTab = selectedTab,
        selectSearchButton = selectSearch,
        selectedPoster = selectedPoster,
        logOutAction = logOutAction
    )
}

@Composable
fun MainScreen(
    bottomBarState: State<Boolean>,
    bottomNavOffset: Dp,
    bottomNavAlpha: Float,
    tabs: List<HomeTab>,
    selectedTab: HomeTab,
    viewModel: MainViewModel,
    logOutAction: () -> Unit,
    selectSearchButton: () -> Unit,
    selectedPoster: (PokeMark?) -> Unit
) {
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (topBarRef, contentRef, bottomBarRef, fabRef) = createRefs()

        PokemonAppBar(
            modifier = Modifier
                .constrainAs(topBarRef) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            onLogoutClick = logOutAction
        )

        AnimatedVisibility(
            visible = bottomBarState.value,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it }),
            modifier = Modifier.constrainAs(bottomBarRef) {
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        ) {
            BottomNavigation(
                modifier = Modifier
                    .offset(y = bottomNavOffset)
                    .alpha(bottomNavAlpha),
                elevation = 8.dp,
                backgroundColor = MaterialTheme.colorScheme.surfaceTint
            ) {
                tabs.forEach { tab ->
                    BottomNavigationItem(
                        selected = tab == selectedTab,
                        onClick = { viewModel.selectTab(tab.title) },
                        icon = {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.background
                            )
                        },
                        selectedContentColor = LocalContentColor.current,
                        unselectedContentColor = LocalContentColor.current
                    )
                }
            }
        }

        Crossfade(
            targetState = selectedTab,
            label = "",
            modifier = Modifier.constrainAs(contentRef) {
                top.linkTo(topBarRef.bottom)
                bottom.linkTo(bottomBarRef.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                height = Dimension.fillToConstraints
            }
        ) { destination ->
            when (destination) {
                HomeTab.HOME -> HomePokemon(Modifier.fillMaxSize(), selectedPoster, viewModel)
                HomeTab.ABOUT -> AboutScreen(Modifier.fillMaxSize(), viewModel)
            }
        }

        FloatingActionButton(
            onClick = selectSearchButton,
            modifier = Modifier.constrainAs(fabRef) {
                start.linkTo(parent.start, margin = 16.dp)
                end.linkTo(parent.end, margin = 16.dp)
                bottom.linkTo(bottomBarRef.top, margin = (-30).dp)
            },
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search Pokemon"
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PokemonAppBar(modifier: Modifier, onLogoutClick: () -> Unit) {
    val showDialog = rememberSaveable { mutableStateOf(false) }
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = stringResource(id = R.string.app_name),
                color = MaterialTheme.colorScheme.background,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceTint
        ),
        actions = {
            IconButton(onClick = { showDialog.value = true }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = "Logout",
                    tint = MaterialTheme.colorScheme.background
                )
            }
        }
    )
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Logout") },
            text = { Text("Are you sure you want to log out?") },
            confirmButton = {
                TextButton(onClick = {
                    showDialog.value = false
                    onLogoutClick()
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog.value = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}