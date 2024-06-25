package com.sun.dev.util

import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation


/**
 * Created by SunLion on 2019/11/11.
 *
 * 动画
 */
@Suppress("CAST_NEVER_SUCCEEDS")
class BottomAnimationUtil {

    /**
     * 从底部弹出动画
     */
    fun slideToUp(view: View) {
        val slide = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
            1.0f, Animation.RELATIVE_TO_SELF, 0.0f
        )
        slide.duration = 400
        slide.fillAfter = true
        slide.isFillEnabled = true
        view.startAnimation(slide)


        slide.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {}
            override fun onAnimationRepeat(animation: Animation) {}
        })
    }


}
