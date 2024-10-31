package com.sergiospinola.data.network

import com.sergiospinola.data.model.CharacterListResponse
import retrofit2.http.GET

interface RickMortyAPI {
    @GET("character")
    suspend fun getAllCharacters(): CharacterListResponse
}