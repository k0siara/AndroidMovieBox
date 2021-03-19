package com.patrykkosieradzki.moviebox.domain.repositories

import com.patrykkosieradzki.moviebox.domain.model.MovieDetails
import com.patrykkosieradzki.moviebox.domain.model.MoviesPage


interface MoviesRepository {
    suspend fun getNowPlayingMovies(): MoviesPage
    suspend fun getPopularMovies(page: Int): MoviesPage
    suspend fun getMovieDetails(movieId: Int): MovieDetails
}