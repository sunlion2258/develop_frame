package com.sun.dev.base

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.gyf.immersionbar.ImmersionBar
import com.sun.dev.R

/**
 *  Created by SunLion on 2019/5/3 16:00
 */
abstract class BaseActivity : AppCompatActivity(), CommonMethod {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(initContentViewID())

        onViewCreated()
    }


    open fun onViewCreated() {}
}