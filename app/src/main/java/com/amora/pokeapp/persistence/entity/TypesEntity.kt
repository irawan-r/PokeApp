package com.amora.pokeapp.persistence.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "types")
data class TypesEntity(
	@PrimaryKey
	@ColumnInfo(name = "typeId")
	val typeId: Int? = null,

	@ColumnInfo(name = "fkId")
	val fkId: Int? = null,

	@ColumnInfo(name = "slot")
	val slot: Int? = null,

	@Embedded
	val type: TypeEntity? = null
)