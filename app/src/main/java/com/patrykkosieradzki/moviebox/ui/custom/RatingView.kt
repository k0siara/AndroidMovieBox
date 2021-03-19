package com.patrykkosieradzki.moviebox.ui.custom

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.patrykkosieradzki.moviebox.R

class RatingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var percentage: Int = 0
    var progressCirclePaint: Paint = Paint(ANTI_ALIAS_FLAG)
    val circlePaint: Paint = Paint(ANTI_ALIAS_FLAG).apply {
        color = context.getColor(R.color.very_dark_brown)
    }
    val textPaint: Paint = Paint(ANTI_ALIAS_FLAG).apply {
        color = Color.RED
    }

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.RatingView,
            0, 0
        ).apply {
            try {
                percentage = getInt(R.styleable.RatingView_percentage, 0)
                progressCirclePaint.apply {
                    color = if (percentage < 50) Color.YELLOW else Color.GREEN
                }
            } finally {
                recycle()
            }
        }
        animateProgress()
    }

//    override fun onDraw(canvas: Canvas) {
//        super.onDraw(canvas)
//
//        canvas.apply {
//            drawCircle(
//                (width / 2).toFloat(),
//                (height / 2).toFloat(), width.toFloat() / 2, circlePaint
//            )
//
//            drawText(
//                percentage.toString(), (width / 2).toFloat(),
//                (height / 2).toFloat(), textPaint
//            )
//
// //            val rectF = RectF(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat())
//            drawArc(mRectF, 0F, 180F, false, progressCirclePaint)
//
//            // Draw the shadow
// //
// //            drawOval(shadowBounds, shadowPaint)
// //
// //            // Draw the label text
// //            drawText(data[mCurrentItem].mLabel, textX, textY, textPaint)
// //
// //            // Draw the pie slices
// //            data.forEach {
// //                piePaint.shader = it.mShader
// //                drawArc(bounds,
// //                    360 - it.endAngle,
// //                    it.endAngle - it.startAngle,
// //                    true, piePaint)
// //            }
// //
// //            // Draw the pointer
// //            drawLine(textX, pointerY, pointerX, pointerY, textPaint)
// //            drawCircle(pointerX, pointerY, pointerSize, mTextPaint)
//        }
//    }

    companion object {
        const val ARC_FULL_ROTATION_DEGREE = 360
        const val PERCENTAGE_DIVIDER = 100.0
        const val PERCENTAGE_VALUE_HOLDER = "percentage"
    }

    private var currentPercentage = 0

    private val ovalSpace = RectF()

    private val parentArcColor = Color.GRAY
    private val fillArcColor = Color.GREEN

    private val strokeWidth = 5f
    private val parentArcPaint = Paint().apply {
        style = Paint.Style.STROKE
        isAntiAlias = true
        color = parentArcColor
        strokeWidth = this@RatingView.strokeWidth
    }

    private val fillArcPaint = Paint().apply {
        style = Paint.Style.STROKE
        isAntiAlias = true
        color = fillArcColor
        strokeWidth = this@RatingView.strokeWidth
        strokeCap = Paint.Cap.ROUND
    }

    override fun onDraw(canvas: Canvas?) {
        setSpace()
        canvas?.let {
            drawBackgroundArc(it)
            drawInnerArc(it)
            drawText(it)
        }
    }

    private fun setSpace() {
        val horizontalCenter = (width.div(2)).toFloat()
        val verticalCenter = (height.div(2)).toFloat()
        val ovalSize = width.div(2) - strokeWidth
        ovalSpace.set(
            horizontalCenter - ovalSize,
            verticalCenter - ovalSize,
            horizontalCenter + ovalSize,
            verticalCenter + ovalSize
        )
    }

    private fun drawBackgroundArc(it: Canvas) {
        it.drawArc(ovalSpace, 0f, 360f, false, parentArcPaint)
    }

    private fun drawInnerArc(canvas: Canvas) {
        val percentageToFill = getCurrentPercentageToFill()
        canvas.drawArc(ovalSpace, 270f, percentageToFill, false, fillArcPaint)
    }

    private fun drawText(canvas: Canvas) {
        canvas.drawCircle(
            width.div(2).toFloat(),
            height.div(2).toFloat(), width.div(2).toFloat(), circlePaint
        )
        canvas.drawText(
            "$currentPercentage%",
            (width.div(2)).toFloat(),
            (height.div(2)).toFloat(),
            textPaint
        )
    }

    private fun getCurrentPercentageToFill() =
        (ARC_FULL_ROTATION_DEGREE * (currentPercentage / PERCENTAGE_DIVIDER)).toFloat()

    fun animateProgress() {
        val valuesHolder = PropertyValuesHolder.ofFloat("percentage", 0f, percentage.toFloat())
        val animator = ValueAnimator().apply {
            setValues(valuesHolder)
            duration = 1000
            addUpdateListener {
                val percentage = it.getAnimatedValue(PERCENTAGE_VALUE_HOLDER) as Float
                currentPercentage = percentage.toInt()
                invalidate()
            }
        }
        animator.start()
    }
}
