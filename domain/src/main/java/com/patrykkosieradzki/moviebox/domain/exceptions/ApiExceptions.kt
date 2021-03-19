package com.patrykkosieradzki.moviebox.domain.exceptions

sealed class ApiException(
    open val errorMessage: String,
    val shouldLogout: Boolean
) : RuntimeException(errorMessage) {
    data class UnknownApiException(
        override val errorMessage: String
    ) : ApiException(
        errorMessage,
        false
    )
    data class NetworkError(override val errorMessage: String) : ApiException(errorMessage, false)
    data class OtherError(override val errorMessage: String) : ApiException(errorMessage, false)
}
