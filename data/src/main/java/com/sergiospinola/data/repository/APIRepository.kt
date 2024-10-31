package com.sergiospinola.data.repository

import com.sergiospinola.data.datasources.APIDatasource
import javax.inject.Inject

class APIRepository @Inject constructor(
    private val apiDatasource: APIDatasource
) {
    suspend fun getAllCharacters() = apiDatasource.getAllCharacters()
}