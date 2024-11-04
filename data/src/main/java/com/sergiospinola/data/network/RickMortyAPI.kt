package com.sergiospinola.data.network

import com.sergiospinola.data.model.CharacterData
import com.sergiospinola.data.model.CharacterListResponse
import com.sergiospinola.data.model.EpisodeData
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickMortyAPI {
    @GET("character/")
    suspend fun getAllCharacters(
        @Query("page") page: Int?
    ): CharacterListResponse

    @GET("character/{characterId}")
    suspend fun getCharacter(
        @Path("characterId") characterId: Int
    ): CharacterData

    @GET("episode/{episodeIds}")
    suspend fun getEpisode(
        @Path("episodeIds") episodeIds: String
    ): List<EpisodeData>
}