package com.sun.dev.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sun.dev.R
import com.sun.dev.bean.GyroBean
import com.sun.dev.bean.RedPacketRankBean

/**
 * Created by fengwj on 2024/6/26.
 */
class GyroXYZListAdapter :
    BaseQuickAdapter<GyroBean, BaseViewHolder>(R.layout.item_gyro) {
    override fun convert(holder: BaseViewHolder, bean: GyroBean?) {
        holder.setText(R.id.tv_title, bean!!.name)
    }
}