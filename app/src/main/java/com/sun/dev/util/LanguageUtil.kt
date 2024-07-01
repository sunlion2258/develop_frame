package com.sun.dev.util

import android.content.Context
import android.content.res.Configuration
import com.sun.dev.common.Constants
import java.util.Locale

/**
 * Created by fengwj on 2024/5/10.
 */
class LanguageUtil {
    fun selectLanguage(context: Context, language: String?) {
        //设置语言类型
        val resources = context.resources
        val configuration: Configuration = resources.configuration
        val locale: Locale
        when (language) {
            "en" -> {
                configuration.setLocale(Locale.ENGLISH)
                locale = Locale.ENGLISH
            }

            "zh" -> {
                configuration.setLocale(Locale.SIMPLIFIED_CHINESE)
                locale= Locale.SIMPLIFIED_CHINESE
            }

            "zh-rHK" -> {
                configuration.setLocale(Locale.TRADITIONAL_CHINESE)
                locale = Locale.TRADITIONAL_CHINESE
            }

            "es" -> {
                configuration.setLocale(Locale("es"))
                locale = Locale("es")
            }

            else -> {
                configuration.setLocale(Locale.SIMPLIFIED_CHINESE)
                locale = Locale.SIMPLIFIED_CHINESE
            }
        }


        configuration.locale = locale
        resources.updateConfiguration(configuration, resources.displayMetrics)
        //保存设置语言的类型
        SharedHelper.getEdit { sp -> sp.putString(Constants.SP.LANGUAGE, language) }
    }
}