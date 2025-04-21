package com.amora.pokeapp.repository

import com.amora.pokeapp.persistence.entity.PokemonCompleteDetails
import com.amora.pokeapp.persistence.entity.PokemonDetailsEntity
import com.amora.pokeapp.persistence.entity.PokemonEntity
import com.amora.pokeapp.persistence.entity.SpritesEntity
import com.amora.pokeapp.persistence.entity.StatsEntity
import com.amora.pokeapp.persistence.entity.StatsItemEntity
import com.amora.pokeapp.persistence.entity.TypeEntity
import com.amora.pokeapp.persistence.entity.TypesEntity
import com.amora.pokeapp.persistence.entity.UserEntity
import com.amora.pokeapp.repository.model.PokemonDetails
import com.amora.pokeapp.repository.model.PokemonPoster
import com.amora.pokeapp.repository.model.Sprites
import com.amora.pokeapp.repository.model.Stat
import com.amora.pokeapp.repository.model.StatsItem
import com.amora.pokeapp.repository.model.Types
import com.amora.pokeapp.repository.model.UserAccount


object DataMapper {

    fun List<StatsItem?>?.toListStatsItemEntity(data: PokemonDetails?): List<StatsItemEntity> {
        val expStats = StatsItemEntity(
            fkId = data?.id,
            stats = StatsEntity(name = "exp"),
            baseStat = data?.baseExperience
        )
        return this?.mapNotNull { it?.toStatsItem(data?.id) }?.toMutableList()?.plus(expStats)
            ?: emptyList()
    }

    fun List<Types>?.toListTypeEntity(id: Int?): List<TypesEntity> {
        return this?.map { it.toTypesEntity(id) } ?: emptyList()
    }

    private fun PokemonPoster.toPokemonEntity(pageItem: Int): PokemonEntity {
        return PokemonEntity(page = pageItem, name = name, url = url)
    }

    fun List<PokemonPoster?>.toPokemonsEntity(page: Int): List<PokemonEntity> {
        return this.mapNotNull { it?.toPokemonEntity(page) }
    }

    fun PokemonDetails.toPokemonDetailsEntity(): PokemonDetailsEntity {
        return PokemonDetailsEntity(
            name = name,
            baseExperience = baseExperience,
            weight = weight,
            sprites = sprites?.toSpritesEntity(),
            remoteId = id,
            height = height
        )
    }

    fun PokemonDetails.toPokemonCompleteDetails(): PokemonCompleteDetails {
        return PokemonCompleteDetails(
            pokemonDetails = PokemonDetailsEntity(
                id = id,
                name = name,
                baseExperience = baseExperience,
                weight = weight,
                sprites = sprites?.toSpritesEntity(),
                remoteId = id,
                height = height
            ),
            stats = stats.toListStatsEntity(this),
            types = types.toListTypeEntity(id)
        )
    }

    private fun Sprites.toSpritesEntity(): SpritesEntity {
        return SpritesEntity(frontDefault = frontDefault)
    }

    private fun List<StatsItem?>?.toListStatsEntity(data: PokemonDetails?): List<StatsItemEntity> {
        val expStats = StatsItem(
            stat = Stat(name = "exp"),
            baseStat = data?.baseExperience
        ).toStatsItem(data?.id)
        return this?.mapNotNull { it?.toStatsItem(data?.id) }?.toMutableList()?.plus(expStats)
            ?: emptyList()
    }

    private fun StatsItem.toStatsItem(id: Int?): StatsItemEntity {
        return StatsItemEntity(
            fkId = id,
            stats = StatsEntity(name = this.stat?.name, url = this.stat?.url),
            baseStat = baseStat,
            effort = effort
        )
    }

    private fun Types.toTypesEntity(id: Int?): TypesEntity {
        return TypesEntity(
            fkId = id,
            slot = slot,
            type = TypeEntity(name = type?.name, url = type?.url)
        )
    }

    fun UserAccount?.toUserEntity() =
        UserEntity(
            name = this?.name.orEmpty(),
            pass = this?.pass.orEmpty()
        )
}