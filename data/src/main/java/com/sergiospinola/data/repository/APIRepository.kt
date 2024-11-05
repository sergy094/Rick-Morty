package com.sergiospinola.data.repository

import com.sergiospinola.data.datasources.APIDatasource
import com.sergiospinola.data.model.CharacterData
import com.sergiospinola.data.model.CharacterListResponse
import com.sergiospinola.data.model.EpisodeData
import javax.inject.Inject

class APIRepository @Inject constructor(
    private val apiDatasource: APIDatasource
) {
    suspend fun getAllCharacters(page: Int? = null): CharacterListResponse =
        apiDatasource.getAllCharacters(page)

    suspend fun getFilteredCharacters(
        page: Int? = null,
        name: String? = null,
        status: String? = null,
        species: String? = null,
        type: String? = null,
        gender: String? = null
    ): CharacterListResponse =
        apiDatasource.getFilteredCharacters(page, name, status, species, type, gender)

    suspend fun getCharacter(characterId: Int): CharacterData =
        apiDatasource.getCharacter(characterId)

    suspend fun getEpisode(episodesUrls: List<String>): List<EpisodeData> {
        return if (episodesUrls.size == 1) {
            val episodeId = episodesUrls.first().substringAfterLast("/")
            listOf(apiDatasource.getEpisode(episodeId))
        } else {
            val episodesIds = episodesUrls.joinToString(",") { it.substringAfterLast("/") }
            apiDatasource.getEpisodes(episodesIds)
        }
    }
}