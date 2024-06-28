package com.sun.dev.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sun.dev.common.Constants
import com.sun.dev.util.LanguageUtil
import com.sun.dev.util.SharedHelper

/**
 *  Created by SunLion on 2019/5/3 16:00
 */
abstract class BaseActivity : AppCompatActivity(), CommonMethod {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //重启之后恢复到之前的语言
        LanguageUtil().selectLanguage(this,SharedHelper.getShared().getString(Constants.SP.LANGUAGE,"en"))
        setContentView(initContentViewID())
        onViewCreated()
    }


    open fun onViewCreated() {}
}