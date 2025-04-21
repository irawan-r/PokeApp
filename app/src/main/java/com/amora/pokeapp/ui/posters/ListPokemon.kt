package com.amora.pokeapp.ui.posters

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import com.amora.pokeapp.persistence.entity.PokemonEntity
import com.amora.pokeapp.persistence.entity.PokemonEntity.Companion.toPokemonPoster
import com.amora.pokeapp.repository.model.PokeMark

@Composable
fun ListPokemon(
    modifier: Modifier,
    data: LazyPagingItems<PokemonEntity>,
    selectedPoster: (PokeMark?) -> Unit
) {
    LazyVerticalStaggeredGrid(
        modifier = modifier
            .background(backgroundColor)
            .fillMaxSize(),
        columns = StaggeredGridCells.Fixed(2)
    ) {
        val size = data.itemCount
        items(size) { index ->
            val posterItem = data[index]?.toPokemonPoster()
            PokemonPoster(
                poster = posterItem,
                selectedPoster = selectedPoster
            )
        }
    }
}