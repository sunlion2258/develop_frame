package com.sun.dev.entity

/**
 *       Created by SunLion on 2019/4/30 15:21
 */
class PayDataBean {

    var code: String? = null
    var dateDays: String? = null
    var dateRequest: String? = null
    var money: String? = null
    var state: Int = 0

    companion object {
        //待审核
        const val STATE_CHECK = 0
        //审核失败
        const val STATE_FAIL = 1
        //已完结
        const val STATE_OVER = 2
        //待打款
        const val STATE_WAIT_MONEY = 3
        //待还款
        const val STATE_WAIT_RETURN = 4
        //逾期
        const val STATE_OUT_DAYS = 5
    }
}