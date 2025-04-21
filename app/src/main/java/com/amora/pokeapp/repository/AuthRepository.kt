package com.amora.pokeapp.repository

interface AuthRepository {
    suspend fun login(name: String, pass: String): Boolean
    suspend fun register(name: String, pass: String): Boolean
    suspend fun isLoggedIn(): Boolean
}