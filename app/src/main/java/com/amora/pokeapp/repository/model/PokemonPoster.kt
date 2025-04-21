package com.amora.pokeapp.repository.model

import androidx.compose.runtime.Stable
import com.amora.pokeapp.BuildConfig.BASE_IMG_URL
import com.squareup.moshi.Json

@Stable
data class PokemonPoster(

	var page: Int = 0,

	val remoteId: Int? = 0,

	@Json(name="name")
	val name: String? = null,

	@Json(name="url")
	val url: String? = null
) {
	fun getImageUrl(): String {
		val index = url?.split("/".toRegex())?.dropLast(1)?.last()
		return "${BASE_IMG_URL}$index.png"
	}

	fun getPokeId(): Int? {
		val id = url?.split("/".toRegex())?.dropLast(1)?.last()?.toIntOrNull()
		println("poke id get $id")
		return id
	}
}