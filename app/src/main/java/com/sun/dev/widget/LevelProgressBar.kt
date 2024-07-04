package com.sun.dev.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * Created by fengwj on 2024/7/4.
 */
class LevelProgressBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var maxLevel = 5
    private var currentLevel = 2
    private val paint = Paint().apply {
        color = Color.BLUE
        strokeWidth = 20f
        isAntiAlias = true
    }
    private val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = 40f
        isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val width = width.toFloat()
        val height = height.toFloat()

        // Draw progress bar background
        paint.color = Color.LTGRAY
        canvas.drawRect(0f, height / 2 - 10, width, height / 2 + 10, paint)

        // Draw progress bar foreground
        paint.color = Color.BLUE
        val progressWidth = width * currentLevel / maxLevel
        canvas.drawRect(0f, height / 2 - 10, progressWidth, height / 2 + 10, paint)

        // Draw current level text
        val text = "Level: $currentLevel"
        canvas.drawText(
            text,
            width / 2 - textPaint.measureText(text) / 2,
            height / 2 - 30,
            textPaint
        )
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                val newLevel = (event.x / width * maxLevel).toInt().coerceIn(0, maxLevel)
                if (newLevel != currentLevel) {
                    currentLevel = newLevel
                    invalidate()
                }
            }
        }
        return true
    }
}