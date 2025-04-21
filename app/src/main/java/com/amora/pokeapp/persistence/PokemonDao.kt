package com.amora.pokeapp.persistence

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.amora.pokeapp.persistence.entity.PokemonCompleteDetails
import com.amora.pokeapp.persistence.entity.PokemonDetailsEntity
import com.amora.pokeapp.persistence.entity.PokemonEntity
import com.amora.pokeapp.persistence.entity.StatsItemEntity
import com.amora.pokeapp.persistence.entity.TypesEntity

@Dao
interface PokemonDao {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertPokemons(data: List<PokemonEntity>)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertPokemonDetails(detailsEntity: PokemonDetailsEntity)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertStatsItem(statsItem: List<StatsItemEntity>)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertTypesItem(typesItem: List<TypesEntity>)

	@Transaction
	suspend fun insertCompletePokemonDetails(pokemonCompleteDetails: PokemonCompleteDetails) {
		insertPokemonDetails(pokemonCompleteDetails.pokemonDetails)
		insertStatsItem(pokemonCompleteDetails.stats)
		insertTypesItem(pokemonCompleteDetails.types)
	}

	@Query("SELECT * FROM details WHERE id = :pokeId AND name = :pokeName")
	suspend fun getPokemonDetailsComplete(pokeId: Int, pokeName: String): PokemonCompleteDetails?

	@Query("SELECT * FROM pokemon WHERE pokeId = :pokeId")
	suspend fun getPokemonById(pokeId: Int): PokemonEntity?

	@Query("SELECT * FROM pokemon")
	fun getAllPokemon(): PagingSource<Int, PokemonEntity>

	@Query("SELECT * FROM details WHERE name = :name")
	suspend fun getPokemonDetails(name: String): PokemonDetailsEntity?

	@Query("DELETE FROM pokemon")
	suspend fun deletePokemonsList()
}