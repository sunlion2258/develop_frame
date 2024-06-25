package com.sun.dev.util

import android.content.Context
import android.view.View

/**
 *  Created by SunLion on 2019/4/30 11:05
 *  状态栏类
 */
object SizeUtil {

    fun getStatusHeight(context: Context): Int {
        val statusHeightId = context.applicationContext.resources.getIdentifier("status_bar_height", "dimen", "android")
        return context.applicationContext.resources.getDimensionPixelSize(statusHeightId)
    }
}

/**
 * 使View适配透明状态栏
 *
 * 将该View的高度+状态栏高度
 */
fun View.fitTransparentStatus() {
    val statusHeight = SizeUtil.getStatusHeight(this.context)
    val lp = this.layoutParams
    layoutParams.height += statusHeight
    this.layoutParams = lp
}