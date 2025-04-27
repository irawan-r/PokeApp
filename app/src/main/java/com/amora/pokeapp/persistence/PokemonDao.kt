package com.amora.pokeapp.persistence

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.amora.pokeapp.persistence.entity.AbilityEntity
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

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertAbilityItem(abilityItem: List<AbilityEntity>)

	@Transaction
	suspend fun insertCompletePokemonDetails(pokemonCompleteDetails: PokemonCompleteDetails) {
		insertPokemonDetails(pokemonCompleteDetails.pokemonDetails)
		insertStatsItem(pokemonCompleteDetails.stats)
		insertTypesItem(pokemonCompleteDetails.types)
		insertAbilityItem(pokemonCompleteDetails.abilities)
	}

	@Transaction
	@Query("SELECT * FROM details WHERE remote_id = :pokeId OR name = :pokeName")
	suspend fun getPokemonDetailsComplete(pokeId: Int, pokeName: String): PokemonCompleteDetails?

	@Query("SELECT * FROM pokemon WHERE pokeId = :pokeId")
	suspend fun getPokemon(pokeId: Int): PokemonEntity?

	@Query("SELECT * FROM pokemon WHERE pokeId = :pokeId OR name = :pokeName")
	suspend fun searchPokemon(pokeId: Int, pokeName: String): PokemonEntity?

	@Query("SELECT * FROM pokemon")
	fun getAllPokemon(): PagingSource<Int, PokemonEntity>

	@Query("SELECT * FROM details WHERE name = :name")
	suspend fun getPokemonDetails(name: String): PokemonDetailsEntity?

	@Query("DELETE FROM pokemon")
	suspend fun deletePokemonsList()
}