package com.sun.dev.base

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.gyf.immersionbar.ImmersionBar
import com.sun.dev.R
import com.sun.dev.common.Constants
import com.sun.dev.util.LanguageUtil
import com.sun.dev.util.SharedHelper
import java.util.Locale

/**
 *  Created by SunLion on 2019/5/3 16:00
 */
abstract class BaseActivity : AppCompatActivity(), CommonMethod {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(initContentViewID())
        //重启之后恢复到之前的语言
        LanguageUtil().selectLanguage(this,SharedHelper.getShared().getString(Constants.SP.LANGUAGE,"en"))
        onViewCreated()
    }


    open fun onViewCreated() {}
}