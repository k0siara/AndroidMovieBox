package com.patrykkosieradzki.moviebox.utils

import androidx.annotation.DrawableRes

interface ImageWrapper {
    @get:DrawableRes
    val resourceId: Int?
    val imageUrl: String?
}

data class GlideImageWrapper(
    override val resourceId: Int? = null,
    override val imageUrl: String? = null
) : ImageWrapper

fun String?.toGlideImageWrapper() = GlideImageWrapper(imageUrl = this)
