package com.amora.pokeapp.repository.model

import com.squareup.moshi.Json

data class PokemonResponse(

	@Json(name="next")
	val next: String? = null,

	@Json(name="previous")
	val previous: String? = null,

	@Json(name="count")
	val count: Int? = null,

	@Json(name="results")
	val results: List<PokemonPoster?>? = null
)
