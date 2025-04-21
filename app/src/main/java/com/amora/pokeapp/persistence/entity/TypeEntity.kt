package com.amora.pokeapp.persistence.entity

import androidx.room.ColumnInfo

data class TypeEntity(
	@ColumnInfo(name = "name")
	val name: String? = null,

	@ColumnInfo(name = "url")
	val url: String? = null
)