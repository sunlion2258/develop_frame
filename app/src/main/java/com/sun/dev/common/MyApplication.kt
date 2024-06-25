package com.sun.dev.common

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context


/**
 * Created by SunLion on 2019/5/6 13:43
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        //初始化全局Context
        context = this.applicationContext
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }
}