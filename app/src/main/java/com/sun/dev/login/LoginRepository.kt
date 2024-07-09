package com.sun.dev.login

import com.sun.dev.entity.CodeBean
import com.sun.dev.entity.RegisterBean
import com.sun.dev.util.doInBackground
import com.sun.dev.net.RetrofitManager
import io.reactivex.Observable

/**
 *  Created by SunLion on 2019/5/6 14:15
 */
class LoginRepository {

    fun getPhoneCode(phoneNumber: String):Observable<CodeBean> {
        return RetrofitManager.getPhoneCode(phoneNumber).doInBackground()
    }

    fun beginLogin(phoneNumber: String, code: String, phoneName: String):Observable<RegisterBean> {
        return RetrofitManager.beginLogin(phoneNumber,code,phoneName)
    }
}