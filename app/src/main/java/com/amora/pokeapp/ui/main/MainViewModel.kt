package com.amora.pokeapp.ui.main

import androidx.annotation.StringRes
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.amora.pokeapp.persistence.entity.PokemonEntity
import com.amora.pokeapp.persistence.entity.UserEntity
import com.amora.pokeapp.repository.MainRepository
import com.amora.pokeapp.ui.utils.ConnectivityObserver
import com.amora.pokeapp.ui.utils.InternetStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
	private val mainRepository: MainRepository,
	connectionState: ConnectivityObserver
) : ViewModel() {

	val isOnline = connectionState.internetStatus
		.stateIn(viewModelScope, SharingStarted.Lazily, initialValue = InternetStatus.Idle)

	private val _currentUser: MutableStateFlow<UserEntity?> = MutableStateFlow(null)
	val currentUser = _currentUser.asStateFlow()

	fun getCurrentUser() {
		viewModelScope.launch {
			_currentUser.emit(mainRepository.getCurrentUser())
		}
	}

	private val _selectedTab: MutableState<Int> = mutableIntStateOf(1)
	val selectedTab: State<Int> get() = _selectedTab

	val pokemonList: Flow<PagingData<PokemonEntity>> =
		mainRepository.getPokemons().cachedIn(viewModelScope)

	fun selectTab(@StringRes tab: Int) {
		_selectedTab.value = tab
	}

}