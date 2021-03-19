package com.patrykkosieradzki.moviebox.domain

interface AppConfiguration {
    val debug: Boolean
    val baseApiUrl: String
    val baseImageApiUrl: String
    val apiKey: String
}
