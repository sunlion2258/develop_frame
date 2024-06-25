package com.sun.dev.fragment.home

import android.view.View
import androidx.lifecycle.ViewModel
import com.sun.dev.R
import com.sun.dev.util.CodeUtil
import com.sun.dev.util.toast

/**
 *  Created by SunLion on 2019/4/29 17:57
 */
class HomeViewModel(val repository: HomeRepository) : ViewModel() {

    /**
     * 首页所有按钮点击事件
     */
    val homeOnClick = View.OnClickListener {
        //检查是否登陆//
        if (!CodeUtil.checkIsLogin()) {
            return@OnClickListener
        }
        when (it.id) {
            R.id.toolbar_service -> {
                toast("客服")
            }
            R.id.toolbar_message->{
                toast("消息")
            }
        }
    }
}