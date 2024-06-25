package com.sun.dev.widget

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.widget.ImageView

/**
 *       Created by SunLion on 2019/4/30 11:58
 *
 *       使图片变成圆形
 *       使用方法：ImageView.setImageDrawable(CircularDrawable(bitmap))
 *       或者使用：ImageView.setCircular()  @{ImageView.setCircular()} 在该kt文件最下面定义
 */
class CircularDrawable(private val bitmap: Bitmap) : Drawable() {

    private var paint: Paint = Paint()
    private lateinit var rect: Rect

    init {
        paint.isAntiAlias = true
        val bitmapShader = BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
        paint.shader = bitmapShader
    }

    override fun setBounds(left: Int, top: Int, right: Int, bottom: Int) {
        super.setBounds(left, top, right, bottom)
        rect = Rect(left, top, right, bottom)
    }

    override fun getIntrinsicHeight(): Int {
        return bitmap.height
    }

    override fun getIntrinsicWidth(): Int {
        return bitmap.width
    }

    override fun draw(canvas: Canvas) {
        canvas.drawCircle(
            (intrinsicWidth / 2).toFloat(), (intrinsicHeight / 2).toFloat(),
            (intrinsicWidth / 2).toFloat(), paint
        )
    }

    override fun setAlpha(alpha: Int) {

    }

    //获取透明图
    override fun getOpacity(): Int = PixelFormat.TRANSPARENT

    override fun setColorFilter(colorFilter: ColorFilter?) {
    }
}

/**
 * ImageView的扩展函数，使图片变成圆形
 */
fun ImageView.setCircular() {
    if (this.drawable is BitmapDrawable){
        val bitmap = (this.drawable as BitmapDrawable).bitmap
        this.setImageDrawable(CircularDrawable(bitmap))
    }
}