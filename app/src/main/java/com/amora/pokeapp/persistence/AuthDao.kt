package com.amora.pokeapp.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.amora.pokeapp.persistence.entity.UserEntity

@Dao
interface AuthDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUserAccount(data: UserEntity): Long

    @Transaction
    @Query("SELECT COUNT(*) FROM user WHERE name = :name AND pass = :password")
    suspend fun getUserAccount(name: String, password: String): Long

    @Query("UPDATE user SET isLoggedIn = 1 WHERE name = :name AND pass = :password")
    suspend fun setUserLoggedIn(name: String, password: String)

    @Query("SELECT EXISTS(SELECT 1 FROM user WHERE name = :name AND pass = :password)")
    suspend fun isUserExists(name: String, password: String): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM user WHERE isLoggedIn = 1 LIMIT 1)")
    suspend fun isAnyUserLoggedIn(): Boolean

    @Query("SELECT * FROM user WHERE isLoggedIn = 1 LIMIT 1")
    suspend fun getLoggedInUser(): UserEntity?

    @Query("UPDATE user SET isLoggedIn = 0 WHERE isLoggedIn = 1")
    suspend fun logoutCurrentUser()

}