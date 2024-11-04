package com.sergiospinola.data.model

import com.google.gson.annotations.SerializedName

enum class CharacterGenderTypeData(val text: String) {
    @SerializedName("Male")
    MALE("Male"),
    @SerializedName("Female")
    FEMALE("Female"),
    @SerializedName("Genderless")
    GENDERLESS("Genderless"),
    @SerializedName("unknown")
    UNKNOWN("Unknown")
}