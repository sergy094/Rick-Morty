package com.sergiospinola.feature.home.model

import com.sergiospinola.data.model.CharacterGenderTypeData
import com.sergiospinola.data.model.CharacterStatusTypeData

data class FilterUIModel(
    val name: String? = null,
    val status: CharacterStatusTypeData? = null,
    val species: String? = null,
    val type: String? = null,
    val gender: CharacterGenderTypeData? = null
) {
    fun hasFilterApplied() = name != null || status != null || species != null || type != null || gender != null
}
