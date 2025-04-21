package com.amora.pokeapp.repository.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PokeMark(
	val id: Int = 0,
	val name: String = ""
): Parcelable