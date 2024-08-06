package com.sun.dev.common

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.sun.dev.datebase.AppDatabase


/**
 * Created by SunLion on 2019/5/6 13:43
 */
class MyApplication : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        lateinit var dp: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()
        //初始化全局Context
        context = this.applicationContext
    }
}