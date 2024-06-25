package com.sun.dev.util

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast

/**
 * ToastUtils
 *
 * @author SunLion
 * @blame Android Team
 * @since 2019/9/24
 */
object ToastUtils {
    private var TOAST: Toast? = null

    /**
     * 短时间吐司
     * @param context 上下文
     * @param resourceID 资源ID
     */
    fun show(context: Context, resourceID: Int) {
        show(context, resourceID, Toast.LENGTH_SHORT)
    }

    /**
     * 短时间吐司
     * @param context 上下文
     * @param text 内容
     */
    fun show(context: Context, text: String) {
        show(context, text, Toast.LENGTH_SHORT)
    }

    /**
     * 自定义时长吐司
     * @param context 上下文
     * @param resourceID 资源ID
     * @param duration 时长
     */
    fun show(context: Context, resourceID: Int?, duration: Int) {
        // 用于显示的文字
        val text = context.resources.getString(resourceID!!)
        show(context, text, duration)
    }

    /**
     * 自定义时长吐司
     * @param context 上下文
     * @param text 吐司内容
     * @param duration 时长
     */
    @SuppressLint("ShowToast")
    fun show(context: Context, text: String, duration: Int) {

        if (TOAST == null) {
            Log.d("__show","null")
            TOAST = Toast.makeText(context, text, duration)
        } else {
            Log.d("__show","setText")
            TOAST!!.setText(text)
            TOAST!!.duration = duration
        }

        TOAST!!.show()
        Log.d("__show","show")
    }
}
