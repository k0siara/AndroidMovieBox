package com.patrykkosieradzki.moviebox.utils

import android.app.Dialog
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.patrykkosieradzki.moviebox.R

private const val MESSAGE_ARG = "message"
private const val TITLE_ARG = "title"
private const val CANCEL_BTN_ARG = "cancel"
private const val OK_BTN_ARG = "ok"
private const val TITLE_ARG_RES = "title_res"
private const val TAG_ARG = "tag"

class DefaultDialogFragment : DialogFragment() {

    var callback: (() -> Unit)? = null
    var cancelCallback: (() -> Unit)? = null
    var title: String? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.setCancelable(false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder = AlertDialog.Builder(requireActivity())
            .setTitle(title ?: arguments?.getString(TITLE_ARG))
            .setMessage(arguments?.getCharSequence(MESSAGE_ARG))
            .setPositiveButton(arguments?.getString(OK_BTN_ARG) ?: getString(R.string.ok)) { _, _ ->
                callback?.invoke()
            }
        if (cancelCallback != null) {
            dialogBuilder.setNegativeButton(
                arguments?.getString(CANCEL_BTN_ARG) ?: getString(R.string.cancel)
            ) { _, _ ->
                cancelCallback!!.invoke()
            }
        }
        val titleId = arguments?.getInt(TITLE_ARG_RES)
        if (titleId != null && titleId != 0) {
            dialogBuilder.setTitle(titleId)
        }
        val dialog = dialogBuilder.create()
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

    fun show(manager: FragmentManager) {
        show(manager, arguments?.getString(TAG_ARG))
    }
}

object DialogFragmentFactory {
    private const val ERROR_DIALOG_TAG = "error_dialog"

    fun newErrorInstance(message: String) =
        DefaultDialogFragment().apply {
            arguments = Bundle().apply {
                putString(TAG_ARG, ERROR_DIALOG_TAG)
                putString(MESSAGE_ARG, message)
                putInt(TITLE_ARG_RES, R.string.error)
            }
        }
}