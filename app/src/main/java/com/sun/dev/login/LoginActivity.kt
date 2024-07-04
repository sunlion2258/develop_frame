@file:Suppress("DEPRECATION")

package com.sun.dev.login

import android.os.Bundle
import android.view.KeyEvent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gyf.immersionbar.ImmersionBar
import com.sun.dev.R
import com.sun.dev.base.BaseMVVMActivity
import com.sun.dev.databinding.ActivityLoginBinding

/**
 * 登陆界面
 *
 */
@Suppress("DEPRECATION")
class LoginActivity : BaseMVVMActivity<ActivityLoginBinding, LoginViewModel>() {

    override fun initContentViewID(): Int = R.layout.activity_login

    override fun initViewModel(): LoginViewModel =
        ViewModelProviders.of(
            this,
            LoginVMFactory(this, LoginRepository())
        ).get(LoginViewModel::class.java)

    override fun onMVVMCreated(savedInstanceState: Bundle?) {
        ImmersionBar.with(this)
            .statusBarDarkFont(true)
            .init()

        bindViews.vm = vm
        //使用自定义liveData属性替换
        bindViews.etPhone.addTextChangedListener(vm.phoneTextAfterChange)
        //使用自定义liveData属性替换
        bindViews.etPsd.addTextChangedListener(vm.codeTextAfterChange)
        //监听是否登陆成功
        vm.isLogin.observe(this, Observer {
            if (it) {
                finish()
            }
        })
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK
            && event!!.action == KeyEvent.ACTION_DOWN
        ) {
            finish()
        }
        return super.onKeyDown(keyCode, event)
    }

}
