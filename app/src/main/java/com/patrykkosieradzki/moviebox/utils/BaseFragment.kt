package com.patrykkosieradzki.moviebox.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.addCallback
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat.getColor
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.patrykkosieradzki.moviebox.R
import com.patrykkosieradzki.moviebox.BR
import com.patrykkosieradzki.moviebox.domain.AppConfiguration
import com.patrykkosieradzki.moviebox.domain.exceptions.ApiException
import com.patrykkosieradzki.moviebox.utils.extensions.goneIfWithAnimation
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getViewModel
import timber.log.Timber
import kotlin.reflect.KClass

@Suppress("TooManyFunctions")
abstract class BaseFragment<STATE : ViewState, VM : BaseViewModel<STATE>, VDB : ViewDataBinding>(
    @LayoutRes private val layoutId: Int,
    vmKClass: KClass<VM>
) : Fragment() {

    protected lateinit var binding: VDB

    val viewModel: VM by lazy {
        getViewModel(clazz = vmKClass)
    }

    val appConfiguration: AppConfiguration by inject()

    private var loader: LottieAnimationView? = null

    var onBackEvent: () -> Unit = {
        try {
            findNavController().navigateUp()
        } catch (e: IllegalStateException) {
            Timber.e("Fragment $this is not a NavHostFragment or within a NavHostFragment")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate<VDB>(inflater, layoutId, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                setVariable(BR.vm, viewModel)
            }
        return RelativeLayout(requireContext()).apply {
            layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
            )
            id = R.id.loader_container
            addView(binding.root, ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT))
            loader = LottieAnimationView(requireContext()).apply {
                isClickable = true
                isFocusable = true
                visibility = View.GONE
                setBackgroundColor(getColor(requireContext(), R.color.dark_brown))
                setAnimation(R.raw.lottie_loading_animation)
                repeatMode = LottieDrawable.RESTART
                repeatCount = LottieDrawable.INFINITE
                playAnimation()
            }
            addView(loader, RelativeLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT))
        }
    }

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewModel) {
            inProgress.observe(viewLifecycleOwner) {
                loader?.goneIfWithAnimation(!it)
            }
            showToastEvent.observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
            showErrorEvent.observe(viewLifecycleOwner) {
                showError(it)
            }
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
                onBackEvent.invoke()
            }
            setupViews(view)
            initialize()
        }
    }

    open fun setupViews(view: View) {}

    protected fun showError(error: ErrorEvent) = when (error.throwable) {
        is ApiException -> {
            showApiException(error.throwable, error.isInitialState)
        }
        else -> if (appConfiguration.debug) showErrorDialog(
            error.throwable?.message ?: "Error"
        ) else showErrorDialog(
            getString(R.string.general_error_message)
        )
    }

    private fun showApiException(error: ApiException, isInitialError: Boolean) {
        showErrorDialog(error.errorMessage) {
            when {
                isInitialError -> {
                    try {
                        findNavController().popBackStack()
                    } catch (e: IllegalStateException) {
                        Timber.e("Fragment $this is not a NavHostFragment or within a NavHostFragment")
                    }
                }
            }
        }
    }

    protected fun showErrorDialog(message: String, actionOnDismiss: () -> Unit = {}) {
        Timber.e("showErrorDialog:$message")
        val dialog = DialogFragmentFactory.newErrorInstance(message)
        dialog.callback = {
            actionOnDismiss()
            viewModel.updateViewStateToSuccess()
        }
        dialog.show(childFragmentManager)
    }
}

interface ViewState {
    val inProgress: Boolean
    fun toSuccess(): ViewState
}
