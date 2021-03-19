package com.patrykkosieradzki.moviebox.ui.list

import com.patrykkosieradzki.moviebox.utils.GlideImageWrapper


data class MovieItem(
    val id: Int?,
    val posterUrl: GlideImageWrapper,
    val title: String?,
    val releaseDate: String
)
