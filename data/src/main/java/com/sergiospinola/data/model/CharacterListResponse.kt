package com.sergiospinola.data.model

data class CharacterListResponse (
    val info: CharacterInfoData,
    val results: List<CharacterData>
)