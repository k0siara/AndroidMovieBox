package com.patrykkosieradzki.moviebox.di

import com.patrykkosieradzki.moviebox.MovieBoxAppConfiguration
import com.patrykkosieradzki.moviebox.domain.AppConfiguration
import com.patrykkosieradzki.moviebox.domain.usecases.*
import com.patrykkosieradzki.moviebox.ui.details.MovieDetailsViewModel
import com.patrykkosieradzki.moviebox.ui.list.MovieListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<AppConfiguration> {
        MovieBoxAppConfiguration()
    }

    factory<GetNowPlayingMoviesUseCase> {
        GetNowPlayingMoviesUseCaseImpl(
            moviesRepository = get()
        )
    }

    factory<GetPopularMoviesUseCase> {
        GetPopularMoviesUseCaseImpl(
            moviesRepository = get()
        )
    }

    factory<GetMovieDetailsUseCase> {
        GetMovieDetailsUseCaseImpl(
            moviesRepository = get()
        )
    }

    viewModel {
        MovieListViewModel(
            getNowPlayingMoviesUse = get(),
            getPopularMoviesUseCase = get(),
            appConfiguration = get()
        )
    }

    viewModel {
        MovieDetailsViewModel(
            getMovieDetailsUseCase = get(),
            appConfiguration = get()
        )
    }
}
