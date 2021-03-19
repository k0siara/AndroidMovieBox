package com.patrykkosieradzki.moviebox.network.utils

import com.patrykkosieradzki.moviebox.domain.AppConfiguration
import mu.KotlinLogging
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

interface OkHttpClientFactory {
    fun createOkHttpClient(): OkHttpClient
}

class CustomOkHttpClientFactory(
    private val appConfiguration: AppConfiguration
) : OkHttpClientFactory {
    private val logger = KotlinLogging.logger("OkHttp")

    override fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            if (appConfiguration.debug) {
                val loggingInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                    override fun log(message: String) {
                        println(message)
                        logger.debug { message }
                    }
                }).apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
                addInterceptor(loggingInterceptor)
                addInterceptor(ApiKeyInterceptor(appConfiguration))
            }
        }.build()
    }
}


