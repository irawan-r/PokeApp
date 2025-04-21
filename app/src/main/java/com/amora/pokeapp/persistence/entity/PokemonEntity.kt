package com.amora.pokeapp.persistence.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.amora.pokeapp.repository.model.PokemonPoster

@Entity(tableName = "pokemon")
class PokemonEntity(
	@PrimaryKey
	@ColumnInfo(name = "pokeId")
	val pokeId: Int? = null,

	@ColumnInfo(name = "page")
	var page: Int = 0,

	@ColumnInfo("name")
	val name: String? = null,

	@ColumnInfo("url")
	val url: String? = null
) {
	private fun getPokemonId(): Int? {
		val id = url?.split("/".toRegex())?.dropLast(1)?.last()?.toIntOrNull()
		return id
	}

	companion object {
		fun PokemonEntity.toPokemonPoster(): PokemonPoster {
			return PokemonPoster(page = page, name = name, url = url, remoteId = getPokemonId())
		}
	}
}