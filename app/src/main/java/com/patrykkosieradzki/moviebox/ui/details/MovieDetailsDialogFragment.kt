package com.patrykkosieradzki.moviebox.ui.details

import android.os.Bundle
import android.view.View
import com.patrykkosieradzki.moviebox.R
import com.patrykkosieradzki.moviebox.databinding.MovieDetailsDialogFragmentBinding
import com.patrykkosieradzki.moviebox.utils.BaseFullScreenDialogFragment

class MovieDetailsDialogFragment :
    BaseFullScreenDialogFragment<MovieDetailsViewState,
            MovieDetailsViewModel, MovieDetailsDialogFragmentBinding>(
        R.layout.movie_details_dialog_fragment, MovieDetailsViewModel::class
    ) {

    override fun setupViews(view: View) {
        super.setupViews(view)
        with(viewModel) {
            closeDialogEvent.observe(viewLifecycleOwner) {
                dialog?.dismiss()
            }
        }
        arguments?.let {
            val movieId = it.getInt(MOVIE_ID_ARG)
            viewModel.loadMovieDetails(movieId)
        }
    }

    companion object {
        private const val MOVIE_ID_ARG = "MOVIE_ID"

        fun newInstance(movieId: Int) = MovieDetailsDialogFragment().apply {
            arguments = Bundle().apply {
                putInt(MOVIE_ID_ARG, movieId)
            }
        }
    }
}
