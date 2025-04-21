package com.amora.pokeapp.repository.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.amora.pokeapp.network.ApiService
import com.amora.pokeapp.persistence.PokemonDatabase
import com.amora.pokeapp.persistence.entity.PokemonEntity
import com.amora.pokeapp.persistence.entity.RemoteKeys
import com.amora.pokeapp.repository.DataMapper.toPokemonsEntity
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class PokemonRemoteMediator @Inject constructor(
	private val database: PokemonDatabase,
	private val apiService: ApiService,
	private var page: Int
): RemoteMediator<Int, PokemonEntity>() {

	companion object {
		const val INITIAL_PAGE_INDEX = 0
		const val PAGE_SIZE = 10
	}

	override suspend fun initialize(): InitializeAction {
		return InitializeAction.LAUNCH_INITIAL_REFRESH
	}

	override suspend fun load(
		loadType: LoadType,
		state: PagingState<Int, PokemonEntity>
	): MediatorResult {
		page = when (loadType) {
			LoadType.REFRESH ->{
				val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
				remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
			}
			LoadType.PREPEND -> {
				val remoteKeys = getRemoteKeyForFirstItem(state)
				val prevKey = remoteKeys?.prevKey
					?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
				prevKey
			}
			LoadType.APPEND -> {
				val remoteKeys = getRemoteKeyForLastItem(state)
				val nextKey = remoteKeys?.nextKey
					?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
				nextKey
			}
		}

		try {
			val responseData = apiService.getAllPokemon(limit = PAGE_SIZE, offset = page * PAGE_SIZE)
			val data = responseData.results
			val endOfPaginationReached = data.isNullOrEmpty()
			database.withTransaction {
				if (loadType == LoadType.REFRESH) {
					database.pokemonDao().deletePokemonsList()
					database.remoteKeysDao().deleteRemoteKeys()
				}
				val prevKey = if (page == 0) null else page  - 1
				val nextKey = if (endOfPaginationReached) null else page + 1
				val keys = responseData.results?.map {
					RemoteKeys(id = it?.name ?: "", prevKey = prevKey, nextKey = nextKey)
				}
				if (keys != null) {
					database.remoteKeysDao().insertAll(keys)
				}
				if (data != null) {
					database.pokemonDao().insertPokemons(data.toPokemonsEntity(page))
				}
			}
			return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
		} catch (exception: Exception) {
			return MediatorResult.Error(exception)
		}
	}

	private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, PokemonEntity>): RemoteKeys? {
		return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
			data.name?.let { database.remoteKeysDao().getRemoteKeysId(it) }
		}
	}
	private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, PokemonEntity>): RemoteKeys? {
		return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
			data.name?.let { database.remoteKeysDao().getRemoteKeysId(it) }
		}
	}
	private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, PokemonEntity>): RemoteKeys? {
		return state.anchorPosition?.let { position ->
			state.closestItemToPosition(position)?.name?.let { id ->
				database.remoteKeysDao().getRemoteKeysId(id)
			}
		}
	}
}