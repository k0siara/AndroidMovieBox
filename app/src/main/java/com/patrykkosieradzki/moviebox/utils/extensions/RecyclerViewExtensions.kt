package com.patrykkosieradzki.moviebox.utils.extensions

import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.addSeparator(@DrawableRes drawable: Int) {
    ContextCompat.getDrawable(context, drawable)?.let {
        addItemDecoration(
            DividerItemDecoration(
                context,
                (layoutManager as LinearLayoutManager).orientation
            ).apply {
                setDrawable(it)
            }
        )
    }
}
