package com.patrykkosieradzki.moviebox.ui.list

import android.view.View
import androidx.lifecycle.lifecycleScope
import com.patrykkosieradzki.moviebox.R
import com.patrykkosieradzki.moviebox.databinding.MovieListFragmentBinding
import com.patrykkosieradzki.moviebox.ui.details.MovieDetailsDialogFragment
import com.patrykkosieradzki.moviebox.utils.BaseFragment
import com.patrykkosieradzki.moviebox.utils.GlideImageWrapper
import com.patrykkosieradzki.moviebox.utils.extensions.addSeparator
import com.patrykkosieradzki.moviebox.utils.extensions.itemBindingOf
import kotlinx.coroutines.launch

class MovieListFragment :
    BaseFragment<MovieListViewState, MovieListViewModel, MovieListFragmentBinding>(
        R.layout.movie_list_fragment, MovieListViewModel::class
    ) {
    val itemBinding = itemBindingOf<GlideImageWrapper>(R.layout.movie_poster_item)

    override fun setupViews(view: View) {
        super.setupViews(view)
        onBackEvent = { requireActivity().moveTaskToBack(true) }
        val moviesAdapter = MoviesAdapter { viewModel.onMovieListItemClicked(it) }
        with(binding) {
            playingNowMoviesBinding = itemBinding
            moviesRecyclerView.apply {
                adapter = moviesAdapter
                addSeparator(R.drawable.list_separator)
            }
        }
        with(viewModel) {
            updatedMovies.observe(viewLifecycleOwner) {
                lifecycleScope.launch {
                    moviesAdapter.submitData(it)
                }
            }
            showDetailsEvent.observe(viewLifecycleOwner) {
                MovieDetailsDialogFragment
                    .newInstance(it)
                    .show(parentFragmentManager, MOVIE_DETAILS_DIALOG_TAG)
            }
        }
    }

    companion object {
        const val MOVIE_DETAILS_DIALOG_TAG = "MovieDetailsDialog"
    }
}
