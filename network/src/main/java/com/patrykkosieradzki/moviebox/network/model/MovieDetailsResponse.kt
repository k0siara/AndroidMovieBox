package com.patrykkosieradzki.moviebox.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieDetailsResponse(
    @Json(name = "genres") val genres: List<MovieGenreResponse>?,
    @Json(name = "id") val id: Int?,
    @Json(name = "overview") val overview: String?,
    @Json(name = "popularity") val popularity: Double?,
    @Json(name = "poster_path") val posterPath: String?,
    @Json(name = "release_date") val releaseDate: String?,
    @Json(name = "runtime") val runtime: Int?,
    @Json(name = "title") val title: String?,
)

@JsonClass(generateAdapter = true)
data class MovieGenreResponse(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String
)
