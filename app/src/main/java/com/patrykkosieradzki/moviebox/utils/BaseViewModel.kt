package com.patrykkosieradzki.moviebox.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import com.patrykkosieradzki.moviebox.utils.extensions.fireEvent
import com.patrykkosieradzki.moviebox.utils.extensions.readOnly
import com.patrykkosieradzki.moviebox.utils.extensions.valueNN
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

@AllOpen
abstract class BaseViewModel<STATE : ViewState>(
    private val initialState: STATE
) : ViewModel() {

    @Suppress("VariableNaming")
    protected val _viewState: MutableLiveData<STATE> = MutableLiveData(initialState)
    val viewState: LiveData<STATE> = _viewState.readOnly
    val inProgress: LiveData<Boolean> = _viewState.map { it.inProgress }
    val showErrorEvent = LiveEvent<ErrorEvent>()
    val showToastEvent = LiveEvent<String>()
    val isInitialState: Boolean
        get() = _viewState.value === initialState
    protected val state: STATE
        get() = viewState.valueNN

    protected val handler = CoroutineExceptionHandler { _, exception ->
        Timber.e(exception, COROUTINE_EXCEPTION_HANDLER_MESSAGE)
        val exceptionToShow = updateError(exception)
        if (exceptionToShow.throwable != null) {
            showErrorEvent.fireEvent(exceptionToShow)
        }
    }

    open fun initialize() {}

    protected fun updateError(exception: Throwable): ErrorEvent {
        return ErrorEvent(exception, isInitialState && _viewState.valueNN.inProgress)
    }

    protected fun updateViewState(update: (STATE) -> STATE) {
        val newState = update(_viewState.valueNN)
        if (newState != _viewState.value) {
            _viewState.value = newState
        }
    }

    fun updateViewStateToSuccess() {
        updateViewState {
            @Suppress("UNCHECKED_CAST")
            it.toSuccess() as STATE
        }
    }

    protected fun safeLaunch(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(handler, block = block)
    }

    protected fun showToast(text: String) {
        showToastEvent.fireEvent(text)
    }

    companion object {
        private const val COROUTINE_EXCEPTION_HANDLER_MESSAGE = "ExceptionHandler"
    }
}

data class ErrorEvent(
    val throwable: Throwable? = null,
    val isInitialState: Boolean = false
)
