package com.amora.pokeapp.ui.details

import androidx.lifecycle.ViewModel
import com.amora.pokeapp.repository.MainRepository
import com.amora.pokeapp.repository.model.PokeMark
import com.amora.pokeapp.ui.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class DetailsViewModel @Inject constructor(
	private val repository: MainRepository
) : ViewModel() {
	private val pokeNameSharedFlow: MutableSharedFlow<PokeMark> = MutableSharedFlow(replay = 1)

	val posterDetailsFlow = pokeNameSharedFlow.flatMapConcat { pokeMark ->
		flow {
			emit(UiState.Loading)
			val result = repository.getPokemonDetails(pokeMark).firstOrNull()
			if (result != null) {
				emit(UiState.Success(data = result))
			} else {
				emit(UiState.Error("Woops, try another pokemon."))
			}
		}
	}

	val posterSearchFlow = pokeNameSharedFlow.flatMapConcat { pokeMark ->
		flow {
			emit(UiState.Loading)
			delay(500)
			val result = repository.getPokemonDetails(pokeMark, true).firstOrNull()
			if (result != null) {
				emit(UiState.Success(data = result))
			} else {
				emit(UiState.Error("Woops, try another pokemon."))
			}
		}
	}

	fun loadPosterByName(poke: PokeMark) {
		pokeNameSharedFlow.tryEmit(poke)
	}
}