package com.sun.dev.fragment.payback

import android.os.Build
import androidx.annotation.RequiresApi
import com.sun.dev.entity.PayDataBean
import java.util.*

/**
 *  Created by SunLion on 2019/4/29 18:03
 *  还款数据仓库
 */
class ContentRepository {
    //测试获取数据
    @RequiresApi(Build.VERSION_CODES.N)
    fun getList(): List<PayDataBean> {
        val list = mutableListOf<PayDataBean>()

        val payDataBean = PayDataBean()
        payDataBean.code="借贷编号：2019040112345678"
        payDataBean.dateDays="借款天数：1期（7天）"
        payDataBean.dateRequest="申请日期 2019-04-01"
        payDataBean.money="1000"
        payDataBean.state=Random().nextInt(6)

        list.add(payDataBean)
        list.add(payDataBean)
        list.add(payDataBean)
        list.add(payDataBean)
        list.add(payDataBean)
        list.add(payDataBean)
        list.add(payDataBean)
        return list
    }
}