package com.patrykkosieradzki.moviebox.ui.details

import com.hadilq.liveevent.LiveEvent
import com.patrykkosieradzki.moviebox.domain.AppConfiguration
import com.patrykkosieradzki.moviebox.domain.usecases.GetMovieDetailsUseCase
import com.patrykkosieradzki.moviebox.utils.BaseViewModel
import com.patrykkosieradzki.moviebox.utils.GlideImageWrapper
import com.patrykkosieradzki.moviebox.utils.ViewState
import com.patrykkosieradzki.moviebox.utils.extensions.EMPTY
import com.patrykkosieradzki.moviebox.utils.extensions.fireEvent
import com.patrykkosieradzki.moviebox.utils.extensions.toFormattedDate
import com.patrykkosieradzki.moviebox.utils.toGlideImageWrapper

class MovieDetailsViewModel(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val appConfiguration: AppConfiguration
) : BaseViewModel<MovieDetailsViewState>(
    initialState = MovieDetailsViewState(inProgress = true)
) {
    val closeDialogEvent = LiveEvent<Unit>()

    fun loadMovieDetails(movieId: Int) {
        safeLaunch {
            val movieDetails = getMovieDetailsUseCase(movieId)
            updateViewState {
                it.copy(
                    posterUrl = "${appConfiguration.baseImageApiUrl}${movieDetails.posterPath}".toGlideImageWrapper(),
                    title = movieDetails.title ?: EMPTY,
                    overview = movieDetails.overview ?: EMPTY,
                    genres = movieDetails.genres,
                    releaseDate = movieDetails.releaseDate.toFormattedDate(),
                    duration = countDuration(movieDetails.runtime),
                    inProgress = false
                )
            }
        }
    }

    private fun countDuration(runtime: Int?): String {
        return if (runtime == null) {
            EMPTY
        } else {
            val hours = runtime / 60
            val minutesLeft = runtime % 60
            "%dh %dm".format(hours, minutesLeft)
        }
    }

    fun onBackArrowClicked() {
        closeDialogEvent.fireEvent()
    }
}

data class MovieDetailsViewState(
    override val inProgress: Boolean,
    val posterUrl: GlideImageWrapper = "".toGlideImageWrapper(),
    val title: String = "",
    val releaseDate: String = "",
    val duration: String = "",
    val overview: String = "",
    val genres: List<String> = emptyList()
) : ViewState {
    override fun toSuccess() = copy(inProgress = false)
}
