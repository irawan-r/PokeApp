package com.amora.pokeapp.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.amora.pokeapp.network.ApiService
import com.amora.pokeapp.persistence.PokemonDatabase
import com.amora.pokeapp.persistence.entity.PokemonCompleteDetails
import com.amora.pokeapp.persistence.entity.PokemonEntity
import com.amora.pokeapp.persistence.entity.UserEntity
import com.amora.pokeapp.repository.DataMapper.toAbilitiesEntities
import com.amora.pokeapp.repository.DataMapper.toListStatsItemEntity
import com.amora.pokeapp.repository.DataMapper.toListTypeEntity
import com.amora.pokeapp.repository.DataMapper.toPokemonCompleteDetails
import com.amora.pokeapp.repository.DataMapper.toPokemonDetailsEntity
import com.amora.pokeapp.repository.mediator.PokemonRemoteMediator
import com.amora.pokeapp.repository.mediator.PokemonRemoteMediator.Companion.PAGE_SIZE
import com.amora.pokeapp.repository.model.PokeMark
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class MainRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val database: PokemonDatabase
) : MainRepository {
    override fun searchPokemon(name: String, page: Int): Flow<PagingData<PokemonEntity>> {
        return flow {
            val pager = Pager(
                config = PagingConfig(pageSize = PAGE_SIZE),
                remoteMediator = PokemonRemoteMediator(
                    database = database,
                    apiService = apiService,
                    page = page
                ),
                pagingSourceFactory = {
                    database.pokemonDao().getAllPokemon()
                }
            )
            emitAll(pager.flow)
        }.flowOn(Dispatchers.IO)
    }


    override fun getPokemons(
        page: Int
    ): Flow<PagingData<PokemonEntity>> {
        return flow {
            val pager = Pager(
                config = PagingConfig(pageSize = PAGE_SIZE),
                remoteMediator = PokemonRemoteMediator(
                    database = database,
                    apiService = apiService,
                    page = page
                ),
                pagingSourceFactory = {
                    database.pokemonDao().getAllPokemon()
                }
            )
            emitAll(pager.flow)
        }.flowOn(Dispatchers.IO)
    }

    override fun getPokemonDetails(
        poke: PokeMark
    ): Flow<PokemonCompleteDetails?> {
        return flow {
            val pokemonLocal = database.pokemonDao().getPokemonById(poke.id)
            val pokeName = if (pokemonLocal != null) {
                pokemonLocal.name
            } else {
                poke.name
            }
            val pokemonDetails = apiService.getPokemonDetails(pokeName.orEmpty())
            val localPokemonDetails =
                database.pokemonDao().getPokemonDetailsComplete(poke.id, poke.name)
            if (localPokemonDetails != null && pokemonLocal?.pokeId != null && pokemonLocal.pokeId != 0) {
                emit(localPokemonDetails)
            } else {
                pokemonDetails.suspendOnSuccess {
                    val detailsData = PokemonCompleteDetails(
                        pokemonDetails = data.toPokemonDetailsEntity(),
                        stats = data.stats.toListStatsItemEntity(data),
                        types = data.types.toListTypeEntity(data.id),
                        abilities = data.abilities.toAbilitiesEntities(data.id)
                    )
                    database.pokemonDao().insertCompletePokemonDetails(detailsData)
                    emit(data.toPokemonCompleteDetails())
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getCurrentUser(): UserEntity? {
        return database.authDao().getLoggedInUser()
    }

}