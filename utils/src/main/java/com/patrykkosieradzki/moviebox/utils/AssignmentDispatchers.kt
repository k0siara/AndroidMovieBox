package com.patrykkosieradzki.moviebox.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

object AssignmentDispatchers {
    var Main: CoroutineDispatcher = Dispatchers.Main
    var IO: CoroutineDispatcher = Dispatchers.IO
    var Default = Dispatchers.Default

    fun resetAll() {
        Main = Dispatchers.Main
        IO = Dispatchers.IO
        Default = Dispatchers.Default
    }
}