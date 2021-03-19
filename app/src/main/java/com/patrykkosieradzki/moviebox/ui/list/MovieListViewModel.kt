package com.patrykkosieradzki.moviebox.ui.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hadilq.liveevent.LiveEvent
import com.patrykkosieradzki.moviebox.domain.AppConfiguration
import com.patrykkosieradzki.moviebox.domain.usecases.GetNowPlayingMoviesUseCase
import com.patrykkosieradzki.moviebox.domain.usecases.GetPopularMoviesUseCase
import com.patrykkosieradzki.moviebox.utils.BaseViewModel
import com.patrykkosieradzki.moviebox.utils.GlideImageWrapper
import com.patrykkosieradzki.moviebox.utils.ViewState
import com.patrykkosieradzki.moviebox.utils.extensions.fireChange
import com.patrykkosieradzki.moviebox.utils.extensions.fireEvent
import com.patrykkosieradzki.moviebox.utils.toGlideImageWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

class MovieListViewModel(
    private val getNowPlayingMoviesUse: GetNowPlayingMoviesUseCase,
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val appConfiguration: AppConfiguration
) : BaseViewModel<MovieListViewState>(
    initialState = MovieListViewState(inProgress = true)
) {
    val updatedMovies = MutableLiveData<PagingData<MovieItem>>()
    val showDetailsEvent = LiveEvent<Int>()

    val movies: Flow<PagingData<MovieItem>> = Pager(
        PagingConfig(
            enablePlaceholders = true,
            pageSize = MOVIES_PAGE_SIZE,
            initialLoadSize = MOVIES_PAGE_SIZE,
            prefetchDistance = MOVIES_PAGE_SIZE,
        ),
        pagingSourceFactory = {
            MoviePagingSource(
                getPopularMoviesUseCase = getPopularMoviesUseCase,
                appConfiguration = appConfiguration
            )
        }
    ).flow.cachedIn(viewModelScope)

    override fun initialize() {
        super.initialize()

        safeLaunch {
            val nowPlayingMovies = getNowPlayingMoviesUse()
            updateViewState {
                it.copy(
                    nowPlayingMoviesPosterUrls = nowPlayingMovies.results.map { movie ->
                        "${appConfiguration.baseImageApiUrl}${movie.posterPath}".toGlideImageWrapper()
                    },
                    inProgress = false
                )
            }
            movies.collectLatest {
                updatedMovies.fireChange(it)
            }
        }
    }

    fun onMovieListItemClicked(movie: MovieItem) {
        movie.id?.let {
            showDetailsEvent.fireEvent(it)
        }
    }

    companion object {
        const val MOVIES_PAGE_SIZE = 20
    }
}

data class MovieListViewState(
    override val inProgress: Boolean,
    val nowPlayingMoviesPosterUrls: List<GlideImageWrapper> = emptyList()
) : ViewState {
    override fun toSuccess() = copy(inProgress = false)
}
