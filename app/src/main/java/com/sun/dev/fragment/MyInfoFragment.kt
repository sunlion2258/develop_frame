package com.sun.dev.fragment


import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.sun.dev.R
import com.sun.dev.base.BaseFragment
import com.sun.dev.common.Constants
import com.sun.dev.util.SharedHelper
import kotlinx.android.synthetic.main.fragment_my_info.*

/**
 * 我的资料界面
 */
class MyInfoFragment : BaseFragment() {
    override fun initContentViewID(): Int = R.layout.fragment_my_info

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //初始化数据，从sharedPreference中获取
        initData()
    }

    private fun initData() {
        //获取数据
        val phone: String = SharedHelper.getShared().getString(Constants.SP.PHONE_NUM, "")!!
        if (!TextUtils.isEmpty(phone)) {
            val str = phone.substring(0, 3) + "****" + phone.substring(7, 11)
            tv_phone.text = str
        }
    }
}
