package com.sun.dev.login

import android.annotation.SuppressLint
import android.os.CountDownTimer
import android.text.Editable
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sun.dev.common.Constants
import com.sun.dev.dialog.LoadProgressDialog
import com.sun.dev.entity.CodeBean
import com.sun.dev.entity.RegisterBean
import com.sun.dev.util.CodeUtil
import com.sun.dev.util.SharedHelper
import com.sun.dev.util.SimpleTextWatcher
import com.sun.dev.util.toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 *       Created by SunLion on 2019/5/6 14:15
 */
@SuppressLint("CheckResult")
class LoginViewModel(private val activity: LoginActivity, private val repository: LoginRepository) : ViewModel() {

    //登陆界面注册按钮是否能够点击
    val loginClickable = MutableLiveData(true)
    //获取验证码按钮是否可以点击
    val codeBtnClickable = MutableLiveData(true)
    //获取验证码是否成功
    val codeBean = MutableLiveData<CodeBean>()
    //是否登陆成功
    val isLogin = MutableLiveData<Boolean>()
    //号码的长度是为11位
    var isTruePhoneCount = false
    //号码
    var phoneNumber = ""
    //验证码
    var code = ""
    //等待进度条
    private lateinit var dialog: LoadProgressDialog

    init {
        isLogin.value = SharedHelper.getShared().getBoolean(Constants.SP.IS_LOGIN, false)
    }

    /**
     * 开始获取验证码
     */
    private fun doRequestCode() {

        repository.getPhoneCode(phoneNumber).subscribe({
            if (it.code == 208) {
                toast("获取超时")
            }
        }, {
            toast("网络错误")
        })
    }

    /**
     * 开始登陆
     */
    private fun doLogin() {
        loginClickable.value = false
        repository.beginLogin(phoneNumber, code, CodeUtil.getPhoneName())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { dialog.show() }
            .doFinally { dialog.dismiss() }
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                when (it.code) {
                    200 -> {
                        //登陆成功 保存信息
                        isLogin.value = true
                        saveData(it)
                        toast("登录成功")
                    }
                    else -> {
                        toast(it.msg)
                    }
                }
                loginClickable.value = true
            }, {
                toast(it.message)
                it.printStackTrace()
                loginClickable.value = true
            })
    }

    /**
     * 登陆Login按钮点击事件
     */
    val loginBtnClickListener = View.OnClickListener {
        //显示等待框
        if (!::dialog.isInitialized) {
            dialog = LoadProgressDialog(activity, "加载中...", false)
        }
        if (!isTruePhoneCount) {
            toast("请输入合法手机号")
            return@OnClickListener
        }
        if (code.isNotEmpty()) {
            dialog.show()
            doLogin()

            //登陆成功 保存信息
            isLogin.value = true
            toast("登录成功")
            //保存登陆状态
            SharedHelper.getEdit { sp -> sp.putBoolean(Constants.SP.IS_LOGIN, true) }
            //保存电话号码
            SharedHelper.getEdit { sp -> sp.putString(Constants.SP.PHONE_NUM, phoneNumber) }
        } else {
            toast("请输入验证码")
        }
    }


    /**
     *登陆界面验证码按钮点击事件
     */
    val codeBtnClickListener = View.OnClickListener {
        it as AppCompatTextView
        //进行验证码获取
        if (isTruePhoneCount) {
            toast("正在获取验证码")
            it.isClickable = false
            doRequestCode()
        } else {

            toast("请输入正确的手机号码")
            return@OnClickListener
        }
        //使按钮不可点击
        codeBtnClickable.value = false
        codeBtnClickable.value = false
        //开启一个计时器
        val downTimer = object : CountDownTimer(60000, 1000) {
            override fun onFinish() {
                //计时完成,使按钮继续可点击
                codeBtnClickable.value = true
                it.isClickable = true
                it.text = "获取验证码"
            }

            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                //设置按钮的文字为倒计时

                it.text = (millisUntilFinished / 1000).toInt().toString() + "s"
            }
        }
        downTimer.start()
    }

    /**
     * 登陆手机号输入框监听
     */
    val phoneTextAfterChange = object : SimpleTextWatcher() {
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            isTruePhoneCount = s?.length == 11
        }

        override fun afterTextChanged(s: Editable?) {
            phoneNumber = s.toString()
            Log.d("==>", s.toString())
        }
    }

    /**
     * 登陆验证码输入框监听
     */
    val codeTextAfterChange = object : SimpleTextWatcher() {
        override fun afterTextChanged(s: Editable?) {
            code = s.toString()
        }
    }

    /**
     * 登陆成功后保存用户信息
     */
    private fun saveData(it: RegisterBean) {
        //保存登陆状态
        SharedHelper.getEdit { sp -> sp.putBoolean(Constants.SP.IS_LOGIN, true) }
        //保存Token
        SharedHelper.getEdit { sp -> sp.putString(Constants.SP.TOKEN, it.data?.access_app_token) }
        //保存电话号码
        SharedHelper.getEdit { sp -> sp.putString(Constants.SP.PHONE_NUM, it.data?.phone) }
    }
}