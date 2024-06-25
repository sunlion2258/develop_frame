@file:Suppress("UNCHECKED_CAST")

package com.sun.dev.util

import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.ViewModel
import com.sun.dev.common.Constants
import com.sun.dev.common.MyApplication
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.MessageDigest
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


object CodeUtil {

    /**
     * MD5 32加密
     */
    fun encode(text: String): String {
        val instance = MessageDigest.getInstance("MD5")
        val digest = instance.digest(text.toByteArray())
        val sb = StringBuffer()
        for (b in digest) {
            val i = b.toInt() and 0xff
            var hexString = Integer.toHexString(i)
            if (hexString.length < 2) {
                hexString = "0$hexString"
            }
            sb.append(hexString)
        }
        return sb.toString()
    }

    /**
     * 获取手机型号
     */
    fun getPhoneName(): String = android.os.Build.MODEL

    /**
     * 判断是否登陆
     */
    fun checkIsLogin(): Boolean {
        return SharedHelper.getShared().getBoolean(Constants.SP.IS_LOGIN, false)
    }
}

/**
 *  简化RxJava后台转前台流程
 */
fun <T> Observable<T>.doInBackground(): Observable<T> {
    return this.subscribeOn(Schedulers.io())
        .unsubscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

/**
 * 在ViewModel中吐司的封装
 */
fun toast(string: String?) {
    string?.let { ToastUtils.show(MyApplication.context, it, 2000) }
}

/**
 * 在ViewModel中，协程的封装
 */
fun ViewModel.launch(block: suspend () -> Unit, error: suspend (Throwable) -> Unit) =
    GlobalScope.launch(Dispatchers.Main) {
        try {
            block()
        } catch (error: Throwable) {
            error(error)
        }
    }

/**
 * Retrofit返回Call配合协程的封装
 */
suspend fun <T> Call<T>.await(): T {
    return suspendCoroutine<T> {
        this.enqueue(object : Callback<T> {
            override fun onFailure(call: Call<T>, t: Throwable) {
                it.resumeWithException(t)
            }

            override fun onResponse(call: Call<T>, response: Response<T>) {
                it.resume(response.body() as T)
            }
        })
    }
}

/**
 * EditText文字监听
 */
open class SimpleTextWatcher : TextWatcher {
    override fun afterTextChanged(s: Editable?) {}
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
}

