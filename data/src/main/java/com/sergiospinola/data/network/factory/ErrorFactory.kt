package com.sergiospinola.data.network.factory

import com.sergiospinola.data.model.ErrorData
import retrofit2.HttpException

class ErrorFactory {

    companion object {
        fun buildError(exception: Throwable?) =
            if (exception is HttpException) {
                ErrorData(
                    errorCode = exception.code(),
                    errorMessage = exception.message(),
                )
            } else {
                ErrorData()
            }
    }
}