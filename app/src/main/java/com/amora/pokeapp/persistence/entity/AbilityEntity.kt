package com.amora.pokeapp.persistence.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "ability",
    indices = [Index(value = ["name", "fkId"], unique = true)]
)
data class AbilityEntity(
    @PrimaryKey
    val id: Int? = null,
    @ColumnInfo(name = "fkId")
    val fkId: Int? = null,
    val name: String? = null
)