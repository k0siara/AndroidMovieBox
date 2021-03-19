package com.patrykkosieradzki.moviebox.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.patrykkosieradzki.moviebox.databinding.MovieItemBinding
import com.patrykkosieradzki.moviebox.utils.OnItemClickListener

class MoviesAdapter(
    private val onClick: (MovieItem) -> Unit = {}
) : PagingDataAdapter<MovieItem, MoviesAdapter.MovieItemViewHolder>(MovieItemDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieItemViewHolder {
        val binding = MovieItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieItemViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie!!)
        getItem(position)?.let { holder.bind(it) }
    }

    inner class MovieItemViewHolder(
        private val binding: MovieItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: MovieItem): View {
            binding.item = movie
            binding.listener = object : OnItemClickListener<MovieItem> {
                override fun onClick(item: MovieItem) {
                    onClick.invoke(item)
                }
            }
            return binding.root
        }
    }
}
