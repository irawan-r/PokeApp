package com.amora.pokeapp.repository

import androidx.paging.PagingData
import com.amora.pokeapp.persistence.entity.PokemonCompleteDetails
import com.amora.pokeapp.persistence.entity.PokemonEntity
import com.amora.pokeapp.persistence.entity.UserEntity
import com.amora.pokeapp.repository.model.PokeMark
import kotlinx.coroutines.flow.Flow

interface MainRepository {
	fun searchPokemon(name: String, page: Int = 10): Flow<PagingData<PokemonEntity>>

	fun getPokemons(page: Int = 0): Flow<PagingData<PokemonEntity>>

	fun getPokemonDetails(poke: PokeMark, isSearch: Boolean = false): Flow<PokemonCompleteDetails?>

	suspend fun getCurrentUser(): UserEntity?

	suspend fun logOutCurrentUser()
}