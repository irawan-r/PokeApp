package com.amora.pokeapp.di

import android.content.Context
import com.amora.pokeapp.network.ApiService
import com.amora.pokeapp.persistence.PokemonDatabase
import com.amora.pokeapp.repository.AuthRepository
import com.amora.pokeapp.repository.AuthRepositoryImpl
import com.amora.pokeapp.repository.MainRepository
import com.amora.pokeapp.repository.MainRepositoryImpl
import com.amora.pokeapp.ui.utils.ConnectivityObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {


	@Provides
	@Singleton
	fun provideConnectivityObserver(
		@ApplicationContext context: Context
	): ConnectivityObserver {
		return ConnectivityObserver(context)
	}

	@Provides
	@Singleton
	fun provideRepository(
		apiService: ApiService,
		data: PokemonDatabase,
	): MainRepository {
		return MainRepositoryImpl(
			apiService,
			data
		)
	}

	@Provides
	@Singleton
	fun provideAuthRepository(
		data: PokemonDatabase
	): AuthRepository {
		return AuthRepositoryImpl(
			data
		)
	}
}