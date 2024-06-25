package com.sun.dev.common

import com.sun.dev.BuildConfig


/**
 * 常量
 */
internal interface Constants {

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
        //博客地址
        const val BLOG_URL = "https://blog.csdn.net/qq_36255612/article/list/1"
        //退出登录
        const val LOGOUT = "xiaoyu/member/logout"
        //注销账号
        const val DELETE = "xiaoyu/member/delete"
        //创建对话
        const val SESSION_CREATE = "xiaoyu/session/create"
        //查询对话列表
        const val SESSION_LIST = "xiaoyu/session/list"
        //删除对话
        const val SESSION_DELETE = "xiaoyu/session/delete"
        //修改对话
        const val SESSION_UPDATE = "xiaoyu/session/update"
        //清除上下文
        const val CONTEXT_DELETE = "xiaoyu/context/delete"
        //翻译1
        const val SESSION_TRANS = "xiaoyu/session/trans"
        //纠错1
        const val SESSION_CORRECTION = "xiaoyu/session/correction"
        //解析1
        const val SESSION_ANALYSIS = "xiaoyu/session/analysis"
        //获取对话
        const val SESSION_GET = "xiaoyu/session/get"
        //语言列表
        const val SESSION_LANGUAGE = "xiaoyu/session/language"
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

    }

    /**
     * 使用RxBus进行事件监听
     * tips：《强制》必须一对一，不能一对多，否则无法控制是哪一个接收到事件
     * 不要所有的都使用RxBus来控制，建议在页面嵌套深时使用，如果只是单独界面交互，建议使用interface来操作
     */
    object RxBusTag {
        const val LOGIN_BACK = "登录页点击返回"
    }

}
