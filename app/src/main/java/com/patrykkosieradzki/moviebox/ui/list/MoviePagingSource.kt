package com.patrykkosieradzki.moviebox.ui.list

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.patrykkosieradzki.moviebox.domain.AppConfiguration
import com.patrykkosieradzki.moviebox.domain.model.Movie
import com.patrykkosieradzki.moviebox.domain.usecases.GetPopularMoviesUseCase
import com.patrykkosieradzki.moviebox.utils.extensions.toFormattedDate
import com.patrykkosieradzki.moviebox.utils.toGlideImageWrapper
import timber.log.Timber
import java.lang.Exception

class MoviePagingSource(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val appConfiguration: AppConfiguration,
    private val startingPage: Int = 1
) : PagingSource<Int, MovieItem>() {
    override fun getRefreshKey(state: PagingState<Int, MovieItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieItem> {
        return try {
            val pageNumber = params.key ?: startingPage
            Timber.d("Loading images... $pageNumber ${params.loadSize}")
            val movies = getPopularMoviesUseCase.invoke(pageNumber)
            LoadResult.Page(
                data = movies.results.map { it.toMovieItem() },
                prevKey = null, // Only paging forward.
                nextKey = if (pageNumber < movies.totalPages) pageNumber + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    private fun Movie.toMovieItem() = MovieItem(
        id = id,
        posterUrl = "${appConfiguration.baseImageApiUrl}$posterPath".toGlideImageWrapper(),
        title = title,
        releaseDate = releaseDate.toFormattedDate()
    )
}
