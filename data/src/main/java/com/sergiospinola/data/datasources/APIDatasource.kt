package com.sergiospinola.data.datasources

import com.sergiospinola.data.network.RickMortyAPI
import javax.inject.Inject

class APIDatasource @Inject constructor(
    private val api: RickMortyAPI
) {
    suspend fun getAllCharacters(page: Int?) =
        api.getAllCharacters(page)
}