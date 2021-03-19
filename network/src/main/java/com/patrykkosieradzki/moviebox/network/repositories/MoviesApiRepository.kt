package com.patrykkosieradzki.moviebox.network.repositories

import com.patrykkosieradzki.moviebox.domain.model.Movie
import com.patrykkosieradzki.moviebox.domain.model.MovieDates
import com.patrykkosieradzki.moviebox.domain.model.MovieDetails
import com.patrykkosieradzki.moviebox.domain.model.MoviesPage
import com.patrykkosieradzki.moviebox.domain.repositories.MoviesRepository
import com.patrykkosieradzki.moviebox.network.model.*
import com.patrykkosieradzki.moviebox.network.services.MoviesService
import com.patrykkosieradzki.moviebox.network.utils.NetworkHandler

class MoviesApiRepository(
    private val moviesService: MoviesService,
    private val networkHandler: NetworkHandler
) : MoviesRepository {
    override suspend fun getNowPlayingMovies(): MoviesPage {
        return networkHandler.safeNetworkCall {
            moviesService.getNowPlayingMovies()
        }.toDomain()
    }

    override suspend fun getPopularMovies(page: Int): MoviesPage {
        return networkHandler.safeNetworkCall {
            moviesService.getPopularMovies(page = page)
        }.toDomain()
    }

    override suspend fun getMovieDetails(movieId: Int): MovieDetails {
        return networkHandler.safeNetworkCall {
            moviesService.getMovieDetails(movieId)
        }.toDomain()
    }
}

fun MoviesPageResponse.toDomain() = MoviesPage(
    dates = dates?.toDomain(),
    page = page,
    results = results.map { it.toDomain() },
    totalPages = totalPages,
    totalResults = totalResults
)

fun MovieDatesResponse.toDomain() = MovieDates(
    minimum = minimum,
    maximum = maximum
)

fun MovieResponse.toDomain() = Movie(
    adult = adult,
    backdropPath = backdropPath,
    genreIds = genreIds,
    id = id,
    originalLanguage = originalLanguage,
    originalTitle = originalTitle,
    overview = overview,
    popularity = popularity,
    posterPath = posterPath,
    releaseDate = releaseDate,
    title = title,
    video = video,
    voteAverage = voteAverage,
    voteCount = voteCount
)

fun MovieDetailsResponse.toDomain() = MovieDetails(
    genres = genres.toDomain(),
    id = id,
    overview = overview,
    popularity = popularity,
    posterPath = posterPath,
    releaseDate = releaseDate,
    runtime = runtime,
    title = title
)

fun List<MovieGenreResponse>?.toDomain() = this?.map { it.name } ?: emptyList()
