package com.sun.dev.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sun.dev.R
import com.sun.dev.entity.CallLogBean

/**
 * Created by SunLion on 2019/8/28.
 */
@Suppress("UNREACHABLE_CODE")
class CallLogAdapter(mList: MutableList<CallLogBean>) :
    BaseQuickAdapter<CallLogBean, BaseViewHolder>(R.layout.content_call_log,mList) {

    override fun convert(helper: BaseViewHolder, item: CallLogBean?) {
        helper.setText(R.id.tv_name,item!!.name)
            .setText(R.id.tv_number,item.number)
            .setText(R.id.tv_time,item.dateLong)
            .setText(R.id.tv_date,item.date)
    }

}