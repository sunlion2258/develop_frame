package com.sun.dev.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * Created by fengwj on 2024/7/25.
 * 自定义ball小球，模拟陀螺仪、加速度传感器运动轨迹
 */
class BallView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private var ballX = 0f
    private var ballY = 0f


    val paint = Paint().apply {
        color = Color.RED
    }
    fun setBallXY(ballX:Float,ballY: Float){
        this.ballX =ballX
        this.ballY =ballY
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val canvasWidth = width.toFloat()
        val canvasHeight = height.toFloat()
        val ballRadius = 60f

        val centerX = canvasWidth / 2
        val centerY = canvasHeight / 2

        val drawX = centerX + ballX * (canvasWidth / 2 - ballRadius)
        val drawY = centerY - ballY * (canvasHeight / 2 - ballRadius)

        canvas.drawColor(Color.TRANSPARENT)
        canvas.drawCircle(drawX, drawY, ballRadius, paint)
    }
}