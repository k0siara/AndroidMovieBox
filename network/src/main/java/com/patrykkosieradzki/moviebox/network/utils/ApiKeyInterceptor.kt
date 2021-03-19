package com.patrykkosieradzki.moviebox.network.utils

import com.patrykkosieradzki.moviebox.domain.AppConfiguration
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor(
    private val appConfiguration: AppConfiguration
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val httpUrl = chain.request().url.newBuilder()
            .addQueryParameter(
                API_KEY_QUERY_KEY,
                appConfiguration.apiKey
            )
            .addQueryParameter(
                LANGUAGE_QUERY_KEY,
                LANGUAGE_QUERY_VALUE
            ).build()
        return chain.proceed(chain.request().newBuilder().url(httpUrl).build())
    }

    companion object {
        const val API_KEY_QUERY_KEY = "api_key"
        const val LANGUAGE_QUERY_KEY = "language"
        const val LANGUAGE_QUERY_VALUE = "en-US"
    }
}
