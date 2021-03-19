package com.patrykkosieradzki.theanimalapp.utils

import android.os.Environment.DIRECTORY_PICTURES
import android.os.Environment.getExternalStoragePublicDirectory
import android.util.Log
import android.view.View
import androidx.test.runner.screenshot.BasicScreenCaptureProcessor
import androidx.test.runner.screenshot.Screenshot
import com.patrykkosieradzki.theanimalapp.BuildConfig
import java.io.File
import java.io.IOException

private const val TAG = "Screenshots"

fun takeScreenshot(view: View, screenShotName: String) {
    Log.d(TAG, "Taking screenshot of '$screenShotName'")
    val screenCapture = Screenshot.capture(view)
    val processors = setOf(MyScreenCaptureProcessor())
    try {
        screenCapture.apply {
            name = screenShotName
            process(processors)
        }
        Log.d(TAG, "Screenshot taken")
    } catch (ex: IOException) {
        Log.e(TAG, "Could not take a screenshot", ex)
    }
}

class MyScreenCaptureProcessor : BasicScreenCaptureProcessor() {

    init {
        this.mDefaultScreenshotPath = File(
            File(
                getExternalStoragePublicDirectory(DIRECTORY_PICTURES),
                "${BuildConfig.APPLICATION_ID}/${BuildConfig.BUILD_TYPE}"
            ).absolutePath,
            SCREENSHOTS_FOLDER_NAME
        )
    }

    override fun getFilename(prefix: String): String = prefix

    companion object {
        const val SCREENSHOTS_FOLDER_NAME = "screenshots"
    }
}