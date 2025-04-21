package com.amora.pokeapp.repository.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PokeMark(
	val id: Int,
	val name: String
): Parcelable