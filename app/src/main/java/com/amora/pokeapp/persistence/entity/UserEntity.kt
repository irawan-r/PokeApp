package com.amora.pokeapp.persistence.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String = "",
    val pass: String = "",
    val isLoggedIn: Boolean = false
)
