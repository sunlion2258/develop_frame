package com.sun.dev.activity

import android.content.Intent
import android.os.CountDownTimer
import com.gyf.immersionbar.ImmersionBar
import com.sun.dev.R
import com.sun.dev.base.BaseActivity
import com.sun.dev.util.CodeUtil
import kotlinx.android.synthetic.main.activity_welcome.*
import org.jetbrains.anko.startActivity

/**
 * 欢迎页
 */
class WelcomeActivity : BaseActivity() {
    private var timer: CountDownTimer? = null

    override fun initContentViewID(): Int = R.layout.activity_welcome

    override fun onViewCreated() {
        super.onViewCreated()
        ImmersionBar.with(this)
            .statusBarDarkFont(true)
            .init();

        //如果点击Home回到后台，不再展示欢迎页，除非杀掉进程或者返回键返回才展示此界面
        if (intent.flags and Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT != 0) {
            finish()
            return
        }

        val checkIsLogin = CodeUtil.checkIsLogin()
        if (!checkIsLogin) {
            delayToMainActivity()
        } else {
            startActivity<MainActivity>()
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
        timer = object : CountDownTimer(2000, 1000) {
            override fun onTick(sin: Long) {
            }

            override fun onFinish() {
                startActivity<MainActivity>()
                finish()
            }
        }.start()
    }
}