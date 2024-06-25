package com.sun.dev.adapter

import android.content.Context
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sun.dev.R
import com.sun.dev.entity.PayDataBean
/**
 * Created by SunLion on 2019/4/30 15:09
 */
class ContentRecyclerAdapter : BaseQuickAdapter<PayDataBean,BaseViewHolder>(R.layout.item_borrow) {

    private var list = mutableListOf<PayDataBean>()
    private lateinit var context: Context

    override fun convert(helper: BaseViewHolder, item: PayDataBean?) {

        helper.setText(R.id.borrow_money,item!!.money)
            .setText(R.id.borrow_code,item.code)
            .setText(R.id.borrow_date_days,item.dateDays)
            .setText(R.id.borrow_date_request,item.dateRequest)
            .setText(R.id.borrow_money,"￥"+item.money)

        when (item.state) {
            //审核
            PayDataBean.STATE_CHECK -> {
                helper.setBackgroundRes(R.id.borrow_state,R.drawable.item_borrow_state_check_shape)
                    .setText(R.id.borrow_state,"待审核")
            }
            //审核失败
            PayDataBean.STATE_FAIL -> {
                helper.setBackgroundRes(R.id.borrow_state,R.drawable.item_borrow_state_fail_and_over_shape)
                    .setText(R.id.borrow_state,"审核失败")
            }
            //结束流程
            PayDataBean.STATE_OVER -> {
                helper.setBackgroundRes(R.id.borrow_state,R.drawable.item_borrow_state_fail_and_over_shape)
                    .setText(R.id.borrow_state,"已完成")
            }
            //逾期
            PayDataBean.STATE_OUT_DAYS -> {
                helper.setBackgroundRes(R.id.borrow_state,R.drawable.item_borrow_state_out_days_shape)
                    .setText(R.id.borrow_state,"已逾期")
            }
            //等待打款
            PayDataBean.STATE_WAIT_MONEY -> {
                helper.setBackgroundRes(R.id.borrow_state,R.drawable.item_borrow_state_wait4money_shape)
                    .setText(R.id.borrow_state,"待打款")
            }
            //等待还款
            PayDataBean.STATE_WAIT_RETURN -> {
                helper.setBackgroundRes(R.id.borrow_state,R.drawable.item_borrow_state_wait4return_money_shape)
                    .setText(R.id.borrow_state,"待还款")
            }
        }
    }
}