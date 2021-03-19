package com.patrykkosieradzki.moviebox.domain.usecases

import com.patrykkosieradzki.moviebox.domain.model.MoviesPage
import com.patrykkosieradzki.moviebox.domain.repositories.MoviesRepository

interface GetPopularMoviesUseCase {
    suspend operator fun invoke(page: Int): MoviesPage
}

class GetPopularMoviesUseCaseImpl(
    private val moviesRepository: MoviesRepository
) : GetPopularMoviesUseCase {
    override suspend fun invoke(page: Int): MoviesPage {
        return moviesRepository.getPopularMovies(page)
    }
}
