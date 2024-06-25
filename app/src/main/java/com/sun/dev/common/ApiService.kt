package com.sun.dev.common

import android.text.Html
import com.sun.dev.entity.CodeBean
import com.sun.dev.entity.RecordVoiceResultBean
import com.sun.dev.entity.RegisterBean
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

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

    /**
     * blog
     */
    @POST(Constants.URL.BLOG_URL)
    fun getBlog(): Observable<Html>

    /**
     * 获取结果
     */
    @GET(Constants.URL.BASE_URL + "world/get?")
    fun getRecordResultData(@Query("text") string: String): Observable<RecordVoiceResultBean>
}

