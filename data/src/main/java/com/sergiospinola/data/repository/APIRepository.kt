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

    suspend fun getCharacter(characterId: Int): CharacterData =
        apiDatasource.getCharacter(characterId)

    suspend fun getEpisode(episodesUrls: List<String>): List<EpisodeData> {
        val episodesIds = episodesUrls.joinToString(",") { it.substringAfterLast("/") }
        return apiDatasource.getEpisode(episodesIds)
    }
}