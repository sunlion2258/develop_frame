package com.sun.dev.common

import com.sun.dev.entity.CodeBean
import com.sun.dev.entity.RegisterBean
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST

/**
 *  Created by SunLion on 2019/5/6 13:40
 */
interface ApiService {

    /**
     * 获取手机验证码
     */
    @POST(Constants.URL.PHONE_CODE)
    fun getPhoneCode(@Body body: RequestBody): Observable<CodeBean>

    /**
     * 注册
     */
    @POST(Constants.URL.LOGIN)
    fun beginLogin(@Body requestBody: RequestBody): Observable<RegisterBean>
}