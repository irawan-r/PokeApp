package com.amora.pokeapp.persistence.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ability")
data class AbilityEntity(
    @PrimaryKey
    val id: Int? = null,
    @ColumnInfo(name = "fkId")
    val fkId: Int? = null,
    val name: String? = null
)