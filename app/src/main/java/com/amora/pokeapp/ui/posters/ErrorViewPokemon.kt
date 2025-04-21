package com.amora.pokeapp.ui.posters

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.amora.pokeapp.persistence.entity.PokemonEntity

@Composable
fun ErrorViewPokemon(data: LazyPagingItems<PokemonEntity>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Fail to Load",
            color = Color.White
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colorScheme.surfaceTint
            ),
            onClick = { data.retry() }
        ) {
            Text(
                text = "Retry",
                color = Color.White
            )
        }
    }
}