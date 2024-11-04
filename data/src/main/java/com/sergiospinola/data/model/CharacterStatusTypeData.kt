package com.sergiospinola.data.model

import com.google.gson.annotations.SerializedName

enum class CharacterStatusTypeData(val text: String) {
    @SerializedName("Alive")
    ALIVE("Alive"),
    @SerializedName("Dead")
    DEAD("Dead"),
    @SerializedName("unknown")
    UNKNOWN("Unknown")
}