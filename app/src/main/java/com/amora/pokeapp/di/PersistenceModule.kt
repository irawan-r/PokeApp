package com.amora.pokeapp.di

import android.app.Application
import androidx.room.Room
import com.amora.pokeapp.R
import com.amora.pokeapp.persistence.PokemonDao
import com.amora.pokeapp.persistence.PokemonDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PersistenceModule {

	@Provides
	@Singleton
	fun provideDatabase(application: Application): PokemonDatabase {
		return Room.databaseBuilder(
			application,
			PokemonDatabase::class.java,
			application.getString(R.string.database)
		).fallbackToDestructiveMigration()
			.build()
	}

	@Provides
	@Singleton
	fun provideMovieDao(appDatabase: PokemonDatabase): PokemonDao {
		return appDatabase.pokemonDao()
	}
}