package com.patrykkosieradzki.moviebox.domain.usecases

import com.patrykkosieradzki.moviebox.domain.model.MovieDetails
import com.patrykkosieradzki.moviebox.domain.repositories.MoviesRepository


interface GetMovieDetailsUseCase {
    suspend operator fun invoke(movieId: Int): MovieDetails
}

class GetMovieDetailsUseCaseImpl(
    private val moviesRepository: MoviesRepository
) : GetMovieDetailsUseCase {
    override suspend fun invoke(movieId: Int): MovieDetails {
        return moviesRepository.getMovieDetails(movieId)
    }
}