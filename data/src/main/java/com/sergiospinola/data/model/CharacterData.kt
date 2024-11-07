package com.sergiospinola.data.model

data class CharacterData(
    val id: Int,
    val name: String,
    val status: CharacterStatusTypeData,
    val species: String,
    val type: String,
    val gender: CharacterGenderTypeData,
    val origin: LocationData,
    val location: LocationData,
    val image: String,
    val episode: List<String>,
    val url: String,
    val created: String
)
