package com.patrykkosieradzki.moviebox.domain.model

data class MovieDetails(
    val genres: List<String>,
    val id: Int?,
    val overview: String?,
    val popularity: Double?,
    val posterPath: String?,
    val releaseDate: String?,
    val runtime: Int?,
    val title: String?,
)

data class MovieGenre(
    val id: Int,
    val name: String
)
