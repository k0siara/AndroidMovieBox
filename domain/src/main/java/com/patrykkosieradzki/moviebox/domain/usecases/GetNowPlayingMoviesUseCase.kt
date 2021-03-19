package com.patrykkosieradzki.moviebox.domain.usecases

import com.patrykkosieradzki.moviebox.domain.model.MoviesPage
import com.patrykkosieradzki.moviebox.domain.repositories.MoviesRepository

interface GetNowPlayingMoviesUseCase {
    suspend operator fun invoke(): MoviesPage
}

class GetNowPlayingMoviesUseCaseImpl(
    private val moviesRepository: MoviesRepository
) : GetNowPlayingMoviesUseCase {
    override suspend fun invoke(): MoviesPage {
        return moviesRepository.getNowPlayingMovies()
    }
}