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

    @GET("character/")
    suspend fun getFilteredCharacters(
        @Query("page") page: Int?,
        @Query("name") name: String?,
        @Query("status") status: String?,
        @Query("species") species: String?,
        @Query("type") type: String?,
        @Query("gender") gender: String?
    ): CharacterListResponse

    @GET("character/{characterId}")
    suspend fun getCharacter(
        @Path("characterId") characterId: Int
    ): CharacterData

    @GET("episode/{episodeIds}")
    suspend fun getEpisodes(
        @Path("episodeIds") episodeIds: String
    ): List<EpisodeData>

    @GET("episode/{episodeIds}")
    suspend fun getEpisode(
        @Path("episodeIds") episodeIds: String
    ): EpisodeData
}