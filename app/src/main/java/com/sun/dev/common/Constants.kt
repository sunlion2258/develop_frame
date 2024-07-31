package com.sun.dev.common

import com.sun.dev.BuildConfig


/**
 * 常量
 */
object Constants {

    const val DATABASE_NAME = "sphxt_app_database.db"


    /**
     * 网络请求相关
     */
    object URL {
        // Base_Url
        const val BASE_URL = BuildConfig.ApiHost

        //盐
        const val YAN = "tt^hz"

        //获取验证码
        const val PHONE_CODE = "xiaoyu/member/sendSms"
        //登录
        const val LOGIN = "xiaoyu/member/smsLogin"
    }

    /**
     * SharedPreference相关
     */
    object SP {
        //TitleWithContentActivity内容的类型
        const val TITLE_ACTIVITY_TYPE = "content_type"

        //用户是否登陆
        const val IS_LOGIN = "is_login"

        //Token
        const val TOKEN = "token"

        //手机号
        const val PHONE_NUM = "phone_num"

        //是否首次安装
        const val IS_FIRST = "is_first"
        const val VIP_FILE_NAME = "vip_file_name"
        const val LANGUAGE = "language"
        const val THEME_PREFS = "theme_prefs"

    }
}
