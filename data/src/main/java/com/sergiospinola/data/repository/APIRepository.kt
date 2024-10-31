package com.sergiospinola.data.repository

import com.sergiospinola.data.datasources.APIDatasource
import com.sergiospinola.data.model.CharacterListResponse
import javax.inject.Inject

class APIRepository @Inject constructor(
    private val apiDatasource: APIDatasource
) {
    suspend fun getAllCharacters(page: Int? = null): CharacterListResponse =
        apiDatasource.getAllCharacters(page)
}