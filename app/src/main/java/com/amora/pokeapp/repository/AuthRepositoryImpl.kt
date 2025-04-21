package com.amora.pokeapp.repository

import com.amora.pokeapp.persistence.AuthDao
import com.amora.pokeapp.persistence.PokemonDatabase
import com.amora.pokeapp.repository.DataMapper.toUserEntity
import com.amora.pokeapp.repository.model.UserAccount
import kotlinx.coroutines.delay
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val database: PokemonDatabase
): AuthRepository {
    override suspend fun login(name: String, pass: String): Boolean {
        val userData = UserAccount(name, pass)
        val isUserValid = isUserValid(userData, database.authDao())
        database.authDao().setUserLoggedIn(userData.name, userData.pass)
        delay(1000)
        return isUserValid
    }

    override suspend fun register(name: String, pass: String): Boolean {
        val userData = UserAccount(name, pass)
        val isValidRegister =
            database.authDao().isUserExists(name, pass).not() && (name.isNotBlank() || pass.isNotBlank())
        if (isValidRegister) {
            database.authDao().insertUserAccount(userData.toUserEntity())
        }
        delay(1000)
        return isValidRegister
    }

    private suspend fun isUserValid(user: UserAccount, dao: AuthDao): Boolean {
        return dao.getUserAccount(user.name, user.pass) > 0L
    }

    override suspend fun isLoggedIn(): Boolean {
        return database.authDao().isAnyUserLoggedIn()
    }
}