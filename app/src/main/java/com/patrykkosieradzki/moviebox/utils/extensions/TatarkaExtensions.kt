package com.patrykkosieradzki.moviebox.utils.extensions

import androidx.annotation.LayoutRes
import com.patrykkosieradzki.moviebox.utils.OnItemClickListener
import me.tatarka.bindingcollectionadapter2.ItemBinding
import com.patrykkosieradzki.moviebox.BR

fun <T> itemBindingOf(@LayoutRes layoutId: Int) = ItemBinding.of<T>(BR.item, layoutId)

fun <T> ItemBinding<T>.bindClickListener(onClick: (T) -> Unit) = bindExtra(
    BR.listener,
    object : OnItemClickListener<T> {
        override fun onClick(item: T) {
            onClick.invoke(item)
        }
    }
)
