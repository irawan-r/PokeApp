package com.amora.pokeapp.repository.model

import com.squareup.moshi.Json

data class PokemonDetails(

	@Json(name="base_experience")
	val baseExperience: Int? = null,

	@Json(name="weight")
	val weight: Int? = null,

	@Json(name="sprites")
	val sprites: Sprites? = null,

	@Json(name="types")
	val types: List<Types>? = null,

	@Json(name="stats")
	val stats: List<StatsItem?>? = null,

	@Json(name="name")
	val name: String? = null,

	@Json(name="id")
	val id: Int? = null,

	@Json(name="height")
	val height: Int? = null
)

data class StatsItem(

	@Json(name="stat")
	val stat: Stat? = null,

	@Json(name="base_stat")
	val baseStat: Int? = null,

	@Json(name="effort")
	val effort: Int? = null
)

data class Stat(

	@Json(name="name")
	val name: String? = null,

	@Json(name="url")
	val url: String? = null
)

data class Sprites(

	@Json(name="front_default")
	val frontDefault: String? = null,
)

data class Types(

	@Json(name = "slot")
	val slot: Int? = null,

	@Json(name = "type")
	val type: Type? = null
)

data class Type(
	@Json(name = "name")
	val name: String? = null,

	@Json(name = "url")
	val url: String? = null
)
