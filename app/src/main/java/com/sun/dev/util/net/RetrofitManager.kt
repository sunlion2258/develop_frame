@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.sun.dev.util.net

import android.text.Html
import com.google.gson.Gson
import com.sun.dev.common.ApiService
import com.sun.dev.common.Constants
import com.sun.dev.entity.CodeBean
import com.sun.dev.entity.RecordVoiceResultBean
import com.sun.dev.entity.RegisterBean
import com.sun.dev.util.CodeUtil
import com.sun.dev.util.SharedHelper
import io.reactivex.Observable
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by SunLion on 2019/5/6 13:39
 */
object RetrofitManager {

    /**
     * Gson工具
     */
    var gson = Gson()

    /**
     * 设置okhttp
     */
    private var okHttp = OkHttpClient.Builder()
        .addInterceptor {
            val request = it.request().newBuilder()
            SharedHelper.getShared().getString("token", "")?.let { it1 ->
                request.addHeader(
                    "x-client-token",
                    it1
                )
            }
            val response = it.proceed(request.build())

            response
        }

        .addInterceptor(HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        })

    private val apiService: ApiService = getRetrofit().create(ApiService::class.java)

    /**
     * 获取retrofit
     */
    private fun getRetrofit(): Retrofit {

        return Retrofit.Builder()
            .client(okHttp.build())
            .baseUrl(Constants.URL.BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * 封装RequestBody
     */
    private fun getRequestBody(str: String): RequestBody {
        return str.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
    }


    /**
     * 获取验证码
     */
    fun getPhoneCode(phone: String): Observable<CodeBean> {
        val json = JSONObject()
        json.apply {
            this.put("phone", phone)
            this.put(
                "secret",
                CodeUtil.encode(phone.substring(phone.length - 4, phone.length) + Constants.URL.YAN)
            )
            //Log.d("===>",phone.substring(phone.length - 4, phone.length))
        }
        return apiService.getPhoneCode(getRequestBody(json.toString()))
    }

    /**
     * 开始登陆
     */
    fun beginLogin(phoneNumber: String, code: String, phoneName: String): Observable<RegisterBean> {
        val json = JSONObject()
        json.apply {
            this.put("phone", phoneNumber)
            this.put("code", code)
            this.put("devAlias", phoneName)
        }
        return apiService.beginLogin(getRequestBody(json.toString()))
    }

    /**
     *  blog
     */
    fun getBlogData(): Observable<Html> {
        return apiService.getBlog()
    }

    /**
     * 获取录音请求结果
     */
    fun getRecordResultData(str: String): Observable<RecordVoiceResultBean> {
        return apiService.getRecordResultData(str)
    }
}