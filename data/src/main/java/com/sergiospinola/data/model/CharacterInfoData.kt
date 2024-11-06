package com.sergiospinola.data.model

data class CharacterInfoData(
    val count: Int,
    val pages: Int,
    val next: String? = null,
    val prev: String? = null
)
