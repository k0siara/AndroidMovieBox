package com.patrykkosieradzki.moviebox.utils.extensions

import java.text.SimpleDateFormat

const val EMPTY = "-"
val API_DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd")
val DATE_FORMAT = SimpleDateFormat("MMMM d, yyyy")

fun String?.toFormattedDate(): String {
    return if (this.isNullOrEmpty()) {
        EMPTY
    } else {
        DATE_FORMAT.format(API_DATE_FORMAT.parse(this)!!)
    }
}
