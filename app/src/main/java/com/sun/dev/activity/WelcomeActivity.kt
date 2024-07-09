package com.sun.dev.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.CountDownTimer
import com.gyf.immersionbar.ImmersionBar
import com.sun.dev.R
import com.sun.dev.base.BaseActivity
import com.sun.dev.common.Constants
import com.sun.dev.common.MyApplication
import com.sun.dev.datebase.DatabaseProvider
import com.sun.dev.util.CodeUtil
import com.sun.dev.util.SharedHelper
import com.sun.dev.util.toast
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_welcome.tv_skip
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.startActivity
import java.util.Locale


/**
 * 欢迎页
 */
class WelcomeActivity : BaseActivity() {
    private var timer: CountDownTimer? = null

    override fun initContentViewID(): Int = R.layout.activity_welcome

    @SuppressLint("CheckResult")
    override fun onViewCreated() {
        super.onViewCreated()
        //重启之后恢复到之前的语言
        selectLanguage(SharedHelper.getShared().getString(Constants.SP.LANGUAGE, "zh"))

        ImmersionBar.with(this)
            .statusBarDarkFont(true)
            .init()

        //如果点击Home回到后台，不再展示欢迎页，除非杀掉进程或者返回键返回才展示此界面
        if (intent.flags and Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT != 0) {
            finish()
            return
        }

        RxPermissions(this).request(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ).subscribe {
            if (it) {
                val boolean = SharedHelper.getShared().getBoolean(Constants.SP.IS_FIRST, true)
                if (boolean) {

                    GlobalScope.launch {
                        // 初始化数据库
                        MyApplication.dp = DatabaseProvider.getDatabase(this@WelcomeActivity)
                    }
                    SharedHelper.getEdit { sp -> sp.putBoolean(Constants.SP.IS_FIRST, false) }
                }

                val checkIsLogin = CodeUtil.checkIsLogin()
                if (!checkIsLogin) {
                    delayToMainActivity()
                } else {
                    startActivity<MainActivity>()
                }

            } else {
                toast("需要同意存储权限")
                finish()
            }
        }

        tv_skip.setOnClickListener {
            //取消定时器
            timer!!.cancel()
            //跳转
            startActivity<MainActivity>()
            finish()
        }
    }

    /**
     * 延迟
     */
    private fun delayToMainActivity() {
        timer = object : CountDownTimer(0, 1000) {
            override fun onTick(sin: Long) {
            }

            override fun onFinish() {
                startActivity<MainActivity>()
                finish()
            }
        }.start()
    }

    private fun selectLanguage(language: String?) {
        //设置语言类型
        val resources = resources
        val configuration: Configuration = resources.configuration
        val locale: Locale
        when (language) {
            "en" -> {
                configuration.setLocale(Locale.ENGLISH)
                locale = Locale.ENGLISH
            }

            "zh" -> {
                configuration.setLocale(Locale.SIMPLIFIED_CHINESE)
                locale = Locale.SIMPLIFIED_CHINESE
            }

            "zh-rHK" -> {
                configuration.setLocale(Locale.TAIWAN)
                locale = Locale.TAIWAN
            }

            else -> {
                configuration.setLocale(Locale.SIMPLIFIED_CHINESE)
                locale = Locale.SIMPLIFIED_CHINESE
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocale(locale)
            createConfigurationContext(configuration)
        } else {
            configuration.locale = locale
            resources.updateConfiguration(configuration, resources.displayMetrics)
        }
        //保存设置语言的类型
        SharedHelper.getEdit { sp -> sp.putString(Constants.SP.LANGUAGE, language) }
    }
}