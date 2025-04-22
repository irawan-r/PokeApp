package com.amora.pokeapp.persistence.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.amora.pokeapp.BuildConfig

data class PokemonCompleteDetails(
	@Embedded
	val pokemonDetails: PokemonDetailsEntity,

	@Relation(parentColumn = "remote_id", entityColumn = "fkId")
	val stats: List<StatsItemEntity>,

	@Relation(parentColumn = "remote_id", entityColumn = "fkId")
	val types: List<TypesEntity>,

	@Relation(parentColumn = "remote_id", entityColumn = "fkId")
	val abilities: List<AbilityEntity>
) {
	companion object {
		fun PokemonCompleteDetails.getImageUrl(): String {
			val index = pokemonDetails.remoteId
			return "${BuildConfig.BASE_IMG_URL}$index.png"
		}
	}
}