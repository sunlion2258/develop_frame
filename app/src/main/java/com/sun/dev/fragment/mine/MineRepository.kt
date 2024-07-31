package com.sun.dev.fragment.mine

import com.sun.dev.common.Constants
import com.sun.dev.entity.RegisterBean
import com.sun.dev.util.SharedHelper
import com.sun.dev.net.RetrofitManager
import io.reactivex.Observable

/**
 * Created by SunLion on 2019/4/29 18:03
 * 我的数据仓库
 */
class MineRepository {

    fun getAccount(): String {
        val phone: String = SharedHelper.getShared().getString(Constants.SP.PHONE_NUM, "")!!
        return if (phone.length!=11){
            ""
        }else{
            val str = "账号：" + phone.substring(0, 3) + " " + phone.substring(3, 7) + " " + phone.substring(7, 11)
            str
        }
    }
}