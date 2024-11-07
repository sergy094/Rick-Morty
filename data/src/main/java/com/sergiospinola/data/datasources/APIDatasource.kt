package com.sergiospinola.data.datasources

import com.sergiospinola.data.network.RickMortyAPI
import javax.inject.Inject

class APIDatasource @Inject constructor(
    private val api: RickMortyAPI
) {
    suspend fun getAllCharacters(page: Int?) =
        api.getAllCharacters(page)

    suspend fun getFilteredCharacters(
        page: Int?,
        name: String?,
        status: String?,
        species: String?,
        type: String?,
        gender: String?
    ) = api.getFilteredCharacters(page, name, status, species, type, gender)

    suspend fun getCharacter(characterId: Int) =
        api.getCharacter(characterId)

    suspend fun getEpisodes(episodeId: String) =
        api.getEpisodes(episodeId)

    suspend fun getEpisode(episodeId: String) =
        api.getEpisode(episodeId)
}