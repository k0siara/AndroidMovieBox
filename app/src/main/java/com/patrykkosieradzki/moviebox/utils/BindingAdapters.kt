package com.patrykkosieradzki.moviebox.utils

import android.graphics.Color
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.flexbox.FlexboxLayout
import com.patrykkosieradzki.moviebox.R
import com.patrykkosieradzki.moviebox.utils.extensions.EMPTY
import com.patrykkosieradzki.moviebox.utils.extensions.dpToPx
import java.util.*

@BindingAdapter("onClick")
fun View.setOnClick(action: () -> Unit) {
    setOnClickListener { action.invoke() }
}

@BindingAdapter("visibleInvisible")
fun View.setVisibleInvisible(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("glideImage")
fun ImageView.setGlideImage(glideImageWrapper: GlideImageWrapper) {
    Glide.with(context).load(
        when {
            glideImageWrapper.imageUrl != null -> glideImageWrapper.imageUrl
            glideImageWrapper.resourceId != null -> glideImageWrapper.resourceId
            else -> R.drawable.image_placeholder
        }
    ).placeholder(
        CircularProgressDrawable(context).apply {
            setColorSchemeColors(Color.WHITE)
            strokeWidth = 5f
            centerRadius = 30f
            start()
        }
    ).apply(
        RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
    ).into(this)
}

@BindingAdapter("textChips")
fun FlexboxLayout.setTextChips(items: List<String>) {
    items.forEach {
        addView(
            TextView(context).apply {
                text = it.toUpperCase(Locale.getDefault())
                background = ContextCompat.getDrawable(context, R.drawable.genre_background)
                setTextAppearance(R.style.HelveticaNeue_DarkBrown_12)
                setPadding(8.dpToPx, 5.dpToPx, 8.dpToPx, 5.dpToPx)
                layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                    setMargins(7.dpToPx, 7.dpToPx, 0.dpToPx, 0.dpToPx)
                }
            }
        )
    }
}

@BindingAdapter("nullableText")
fun TextView.setNullableText(nullableText: String?) {
    text = nullableText ?: EMPTY
}
