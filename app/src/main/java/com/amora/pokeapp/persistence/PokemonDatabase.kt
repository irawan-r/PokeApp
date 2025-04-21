package com.amora.pokeapp.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.amora.pokeapp.persistence.entity.PokemonDetailsEntity
import com.amora.pokeapp.persistence.entity.PokemonEntity
import com.amora.pokeapp.persistence.entity.RemoteKeys
import com.amora.pokeapp.persistence.entity.StatsItemEntity
import com.amora.pokeapp.persistence.entity.TypesEntity
import com.amora.pokeapp.persistence.entity.UserEntity

@Database(
	entities = [PokemonEntity::class, StatsItemEntity::class, PokemonDetailsEntity::class, RemoteKeys::class, TypesEntity::class, UserEntity::class],
	version = 1,
	exportSchema = true
)
abstract class PokemonDatabase : RoomDatabase() {

	abstract fun pokemonDao(): PokemonDao

	abstract fun remoteKeysDao(): RemoteKeysDao

	abstract fun authDao(): AuthDao
}