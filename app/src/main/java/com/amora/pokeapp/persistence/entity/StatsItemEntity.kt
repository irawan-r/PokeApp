package com.amora.pokeapp.persistence.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "stats_item",
    indices = [Index("sub_name" , "fkId", unique = true)]
)
data class StatsItemEntity(
    @PrimaryKey
    @ColumnInfo(name = "statId")
    val statId: Int? = null,

    val fkId: Int? = null,

    @Embedded(prefix = "sub_")
    val stats: StatsEntity? = null,

    @ColumnInfo(name = "base_stat")
    val baseStat: Int? = null,

    @ColumnInfo(name = "effort")
    val effort: Int? = null
)