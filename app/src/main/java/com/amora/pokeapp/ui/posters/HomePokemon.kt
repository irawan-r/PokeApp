package com.amora.pokeapp.ui.posters

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.amora.pokeapp.persistence.entity.PokemonEntity
import com.amora.pokeapp.repository.model.PokeMark
import com.amora.pokeapp.repository.model.PokemonPoster
import com.amora.pokeapp.ui.main.MainViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomePokemon(
    modifier: Modifier = Modifier,
    selectedPoster: (PokeMark?) -> Unit,
    viewModel: MainViewModel
) {
    val poster: LazyPagingItems<PokemonEntity> = viewModel.pokemonList.collectAsLazyPagingItems()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = poster.loadState.refresh is LoadState.Loading,
        onRefresh = { poster.refresh() }
    )

    Box(
        modifier = Modifier
            .pullRefresh(pullRefreshState)
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        when {
            poster.itemCount > 0 -> {
                ListPokemon(modifier, poster, selectedPoster)
            }
            poster.loadState.refresh is LoadState.Error -> {
                ErrorViewPokemon(poster)
            }
            poster.loadState.refresh is LoadState.NotLoading -> {
                ListPokemon(modifier, poster, selectedPoster)
            }
            else -> Unit
        }
        PullRefreshIndicator(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .zIndex(2f),
            refreshing = poster.loadState.refresh is LoadState.Loading,
            state = pullRefreshState
        )
    }
}

@Composable
fun PokemonPoster(
    modifier: Modifier = Modifier,
    poster: PokemonPoster?,
    selectedPoster: (PokeMark) -> Unit
) {
    Surface(
        modifier = modifier
            .padding(8.dp)
            .clickable(onClick = {
                val data = PokeMark(
                    id = poster
                        ?.getPokeId() ?: 0,
                    name = poster?.name.orEmpty()
                )
                selectedPoster(data)
            }),
        color = MaterialTheme.colorScheme.primaryContainer,
        shadowElevation = 8.dp,
        shape = MaterialTheme.shapes.large,
    ) {
        ConstraintLayout {
            val (image, title) = createRefs()
            val painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(poster?.getImageUrl())
                    .diskCachePolicy(coil.request.CachePolicy.ENABLED)
                    .memoryCachePolicy(coil.request.CachePolicy.ENABLED)
                    .networkCachePolicy(coil.request.CachePolicy.ENABLED)
                    .crossfade(true)
                    .build()
            )

            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Inside,
                modifier = Modifier
                    .aspectRatio(0.9f)
                    .constrainAs(image) {
                        centerHorizontallyTo(parent)
                        top.linkTo(parent.top)
                    }
            )

            Text(modifier = Modifier
                .constrainAs(title) {
                    centerHorizontallyTo(parent)
                    top.linkTo(image.bottom)
                }
                .padding(8.dp),
                text = poster?.name ?: "Unknown",
                style = MaterialTheme.typography.titleMedium)
        }
    }
}