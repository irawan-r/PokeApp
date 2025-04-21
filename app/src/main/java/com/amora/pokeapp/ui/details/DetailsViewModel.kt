package com.amora.pokeapp.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amora.pokeapp.repository.MainRepository
import com.amora.pokeapp.repository.model.PokeMark
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
	private val repository: MainRepository
) : ViewModel() {
	private val pokeNameSharedFlow: MutableSharedFlow<PokeMark> = MutableSharedFlow(replay = 1)

	@OptIn(ExperimentalCoroutinesApi::class)
	val posterDetailsFlow = pokeNameSharedFlow.flatMapLatest {
		repository.getPokemonDetails(it)
	}

	fun loadPosterByName(poke: PokeMark) {
		pokeNameSharedFlow.tryEmit(poke)
	}
}