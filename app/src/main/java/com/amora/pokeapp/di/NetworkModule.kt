package com.amora.pokeapp.di

import com.amora.pokeapp.BuildConfig
import com.amora.pokeapp.BuildConfig.BASE_URL
import com.amora.pokeapp.network.ApiService
import com.skydoves.sandwich.adapters.ApiResponseCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

	@Provides
	fun provideMoshi(): Moshi {
		return Moshi.Builder()
			.add(KotlinJsonAdapterFactory())
			.build()
	}

	@Provides
	fun provideOkHttpClient(): OkHttpClient {
		val loggingInterceptor = when {
			BuildConfig.DEBUG -> HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
			else -> HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
		}
		return OkHttpClient.Builder()
			.addInterceptor(loggingInterceptor)
			.connectTimeout(10, TimeUnit.SECONDS)
			.readTimeout(10, TimeUnit.SECONDS)
			.build()
	}

	@Provides
	@Singleton
	fun provideApiServices(client: OkHttpClient, moshi: Moshi): ApiService {
		return Retrofit.Builder()
			.addConverterFactory(MoshiConverterFactory.create(moshi))
			.addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
			.baseUrl(BASE_URL)
			.client(client)
			.build()
			.create(ApiService::class.java)
	}
}