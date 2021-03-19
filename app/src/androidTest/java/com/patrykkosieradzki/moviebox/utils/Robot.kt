package com.patrykkosieradzki.moviebox.utils

import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.rule.ActivityTestRule
import com.patrykkosieradzki.theanimalapp.utils.takeScreenshot
import org.hamcrest.CoreMatchers
import org.hamcrest.Matcher
import org.koin.test.KoinTest
import java.lang.reflect.Field
import java.util.concurrent.TimeUnit

open class Robot {

    fun wait(seconds: Int) = TimeUnit.SECONDS.sleep(seconds.toLong())

    fun waitMs(milliseconds: Int) = TimeUnit.MILLISECONDS.sleep(milliseconds.toLong())

    fun capture(tag: String, waitForCaptureInMs: Int = 500, inputToHide: Int = 0) {
        if (screenshotsEnabled) {
            if (inputToHide != 0) {
                Espresso.onView(ViewMatchers.withId(inputToHide)).perform(HideCursorAction())
            }
            waitMs(waitForCaptureInMs)
            val topLevelView = getRecentDecorView(getWindowDecorViews())
            topLevelView?.let {
                takeScreenshot(topLevelView, screenShotName = tag)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun getWindowDecorViews(): Array<View>? {
        val viewsField: Field
        val instanceField: Field
        try {
            viewsField = windowManager!!.getDeclaredField("mViews")
            instanceField = windowManager!!.getDeclaredField(windowManagerString)
            viewsField.isAccessible = true
            instanceField.isAccessible = true
            val instance = instanceField.get(null)
            val arrayListOfViews: ArrayList<View> = viewsField.get(instance) as ArrayList<View>
            return arrayListOfViews.toTypedArray()
        } catch (e: SecurityException) {
            e.printStackTrace()
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

        return null
    }

    private fun getRecentDecorView(views: Array<View>?): View? {
        if (views == null) {
            return null
        }

        val decorViews = arrayOfNulls<View>(views.size)
        var i = 0
        var view: View?

        for (j in views.indices) {
            view = views[j]
            if (view.javaClass.name == "com.android.internal.policy.PhoneWindow\$DecorView" ||
                // required by API 29
                view.javaClass.name == "com.android.internal.policy.DecorView"
            ) {
                decorViews[i] = view
                i++
            }
        }
        return getRecentContainer(decorViews.filterNotNull().toTypedArray())
    }

    private fun getRecentContainer(views: Array<View>): View? {
        var container: View? = null
        var drawingTime: Long = 0
        var view: View?

        for (i in views.indices) {
            view = views[i]
            if (view.isShown && view.hasWindowFocus() && view.drawingTime > drawingTime) {
                container = view
                drawingTime = view.drawingTime
            }
        }
        return container
    }

    companion object {
        var screenshotsEnabled: Boolean = false

        private var windowManagerString = ""
        private var windowManager: Class<*>? = null

        init {
            val windowManagerClassName = if (android.os.Build.VERSION.SDK_INT >= 17) {
                "android.view.WindowManagerGlobal"
            } else {
                "android.view.WindowManagerImpl"
            }
            windowManager = Class.forName(windowManagerClassName)
            windowManagerString = when {
                android.os.Build.VERSION.SDK_INT >= 17 -> "sDefaultWindowManager"
                android.os.Build.VERSION.SDK_INT >= 13 -> "sWindowManager"
                else -> "mWindowManager"
            }
        }
    }
}

abstract class RobotTest<R : Robot> : KoinTest {

    protected fun withRobot(steps: R.() -> Unit) {
        createRobot().apply(steps)
    }

    abstract fun createRobot(): R
}

open class ActivityRobot<T : AppCompatActivity>(protected val rule: ActivityTestRule<T>) : Robot() {
    fun checkWasToastShown(text: String) {
        Espresso.onView(ViewMatchers.withText(text))
            .inRoot(RootMatchers.withDecorView(CoreMatchers.not(rule.activity.window.decorView)))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}

class HideCursorAction : ViewAction {
    override fun getDescription(): String = "Hide cursor"

    override fun getConstraints(): Matcher<View> = CoreMatchers.allOf(
        ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE),
        ViewMatchers.isAssignableFrom(EditText::class.java)
    )

    override fun perform(uiController: UiController?, view: View?) {
        if (view is EditText) {
            view.isCursorVisible = false
        }
    }
}
