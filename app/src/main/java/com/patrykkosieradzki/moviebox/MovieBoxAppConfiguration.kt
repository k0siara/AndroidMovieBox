package com.patrykkosieradzki.moviebox

import com.patrykkosieradzki.moviebox.domain.AppConfiguration

class MovieBoxAppConfiguration : AppConfiguration {
    override val debug = BuildConfig.DEBUG
    override val baseApiUrl = "https://api.themoviedb.org/3/"
    override val baseImageApiUrl = "https://image.tmdb.org/t/p/original"
    override val apiKey = BuildConfig.API_KEY
}
