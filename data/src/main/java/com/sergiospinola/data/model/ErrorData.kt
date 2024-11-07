package com.sergiospinola.data.model

const val GENERIC_ERROR_CODE = -1

data class ErrorData(
    val errorCode: Int? = GENERIC_ERROR_CODE,
    val title: String? = "",
    val errorMessage: String? = "",
)