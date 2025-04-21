package com.amora.pokeapp.persistence.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

data class StatsEntity(
	@ColumnInfo(name = "name")
	val name: String? = null,

	@ColumnInfo(name = "url")
	val url: String? = null
)