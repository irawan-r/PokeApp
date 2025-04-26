package com.amora.pokeapp.persistence.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "types",
    indices = [Index("type_name", "fkId", unique = true)]
)
data class TypesEntity(
    @PrimaryKey
    @ColumnInfo(name = "typeId")
    val typeId: Int? = null,

    @ColumnInfo(name = "fkId")
    val fkId: Int? = null,

    @ColumnInfo(name = "slot")
    val slot: Int? = null,

    @Embedded(prefix = "type_")
    val type: TypeEntity? = null
)