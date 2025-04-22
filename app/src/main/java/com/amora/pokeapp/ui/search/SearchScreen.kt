package com.amora.pokeapp.ui.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.amora.pokeapp.repository.model.PokeMark
import com.amora.pokeapp.ui.details.DetailsViewModel
import com.amora.pokeapp.ui.details.PokemonDetailsBody
import com.amora.pokeapp.ui.utils.onError
import com.amora.pokeapp.ui.utils.onLoading
import com.amora.pokeapp.ui.utils.onSuccess
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

@OptIn(FlowPreview::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: DetailsViewModel,
    pressOnBack: () -> Unit
) {
    val details by viewModel.posterSearchFlow.collectAsState(initial = null)

    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var hasSearched by remember { mutableStateOf(false) }

    LaunchedEffect(searchQuery.text) {
        snapshotFlow { searchQuery.text }
            .debounce(1000)
            .distinctUntilChanged()
            .filter { it.isNotBlank() }
            .collectLatest { query ->
                hasSearched = true
                viewModel.loadPosterByName(PokeMark(name = query))
            }
    }

    Column(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search for a Pokémon...") },
                singleLine = true
            )
        }

        if (!hasSearched && searchQuery.text.isBlank()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Start typing to search for Pokémon.",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        details?.onLoading {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Searching...",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }?.onSuccess { data ->
            data?.let {
                PokemonDetailsBody(
                    poster = data,
                    pressOnBack = pressOnBack,
                    enableSwipe = false
                )
            }
        }?.onError { message ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = message.orEmpty(),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}