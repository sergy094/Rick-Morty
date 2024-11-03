package com.sergiospinola.data.network

import com.sergiospinola.data.model.CharacterListResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface RickMortyAPI {
    @GET("character/")
    suspend fun getAllCharacters(
        @Query("page") page: Int?
    ): CharacterListResponse
}