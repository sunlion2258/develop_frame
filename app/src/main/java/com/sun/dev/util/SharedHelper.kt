package com.sun.dev.util

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.sun.dev.common.MyApplication
import org.jetbrains.anko.defaultSharedPreferences

/**
 *       Created by SunLion on 2019/5/6 13:44
 *
 *       SharedPreference 帮助工具类
 */
object SharedHelper {

    @SuppressLint("CommitPrefEdits")
    fun getEdit(edit: (editor: SharedPreferences.Editor) -> SharedPreferences.Editor) {
        edit(MyApplication.context.defaultSharedPreferences.edit()).commit()
    }

    fun getShared(): SharedPreferences {
        return MyApplication.context.defaultSharedPreferences
    }
}