package com.sun.dev.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sun.dev.R
import com.sun.dev.common.Constants
import com.sun.dev.util.LanguageUtil
import com.sun.dev.util.SharedHelper

/**
 *  Created by SunLion on 2019/5/3 16:00
 */
abstract class BaseActivity : AppCompatActivity(), CommonMethod {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val isDarkTheme = SharedHelper.getShared().getBoolean(Constants.SP.THEME_PREFS, false)
        if (isDarkTheme) {
            setTheme(R.style.AppTheme_Dark)
        } else {
            setTheme(R.style.AppTheme)
        }
        //重启之后恢复到之前的语言
        LanguageUtil().selectLanguage(this,SharedHelper.getShared().getString(Constants.SP.LANGUAGE,"zh"))
        setContentView(initContentViewID())
        onViewCreated()
    }


    open fun onViewCreated() {}
}